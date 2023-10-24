package com.tutorials.unsplashimages.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.google.android.material.snackbar.Snackbar
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.databinding.FragmentAllPhotosBinding
import com.tutorials.unsplashimages.ui.adapter.PhotoLoadingAdapter
import com.tutorials.unsplashimages.ui.adapter.PhotoPagingAdapter
import com.tutorials.unsplashimages.ui.arch.PhotosViewModel
import com.tutorials.unsplashimages.util.PhotoEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class AllPhotos : Fragment() {
    private lateinit var binding: FragmentAllPhotosBinding
    private val viewModel by activityViewModels<PhotosViewModel>()
    private val pagingAdapter: PhotoPagingAdapter by lazy { PhotoPagingAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeToRefresh.setOnRefreshListener {
            pagingAdapter.retry()
            binding.swipeToRefresh.isRefreshing = false
        }

        /*lifecycleScope.launch {
            viewModel.homeFirstRun.collect{
                if (it){
                    viewModel.toggleHomeFirstRun(false)
                    viewModel.getRemoteMediatorPhotos()

                }
            }
        }*/
        //region PAGING3
        binding.apply {
            imagesRv.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadingAdapter { pagingAdapter.retry() },
                footer = PhotoLoadingAdapter { pagingAdapter.retry() }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photoEvent.collect { event ->
                when (event) {
                    is PhotoEvent.Successful -> {
                        showLoadingState(false)
                    }

                    is PhotoEvent.Failure -> {
                        showLoadingState(true)
                    }
                }

            }
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow
                .distinctUntilChangedBy {
                    it.mediator?.refresh
                }
                .filter { it.mediator?.refresh is LoadState.NotLoading }
                .collect { binding.imagesRv.scrollToPosition(0) }
        }

        pagingAdapter.addLoadStateListener { loadstate ->
            when (loadstate.mediator?.refresh) {
                is LoadState.Loading -> {
                    viewModel.checkSizeFromDB()
                }

                is LoadState.Error -> {
                    viewModel.checkSizeFromDB()
                    Snackbar.make(
                        requireView(),
                        "oops! Tap or swipe down to refresh",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Refresh") { pagingAdapter.retry() }.show()
                }

                is LoadState.NotLoading -> {
                    showLoadingState(false)
                    binding.errorText.isVisible = pagingAdapter.itemCount == 0
                }

                else -> Unit
            }

        }

        viewModel.getRemoteMediatorPhotos()
        observeRemoteMediator()


        pagingAdapter.adapterClickListener { imageBody ->
            val route = AllPhotosDirections.actionAllPhotosToViewPhoto(
                imageBody
            )
            findNavController().navigate(route)
        }
        //endregion

    }

    private fun showLoadingState(state:Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun observeRemoteMediator() {
        lifecycleScope.launch {
            viewModel.remoteMediatorPhotos.collect {
                try {
                    updatePagingAdapter(it)

                } catch (e: Exception) {
                    Snackbar.make(
                        requireView(),
                        "oops! Tap or swipe down to refresh",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Refresh") { pagingAdapter.retry() }.show()
                }
            }
        }
    }

    private fun updatePagingAdapter(flow: Flow<PagingData<ImageBody>>) {
        lifecycleScope.launch {
            flow.collect { images ->
                pagingAdapter.submitData(viewLifecycleOwner.lifecycle, images)
            }
        }
    }


}