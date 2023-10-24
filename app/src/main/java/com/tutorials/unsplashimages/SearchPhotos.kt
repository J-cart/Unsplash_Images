package com.tutorials.unsplashimages

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
import com.tutorials.unsplashimages.arch.PhotosViewModel
import com.tutorials.unsplashimages.arch.paging3.PhotoLoadingAdapter
import com.tutorials.unsplashimages.arch.paging3.PhotoPagingAdapter
import com.tutorials.unsplashimages.databinding.FragmentSearchPhotosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class SearchPhotos : Fragment() {
    private lateinit var binding:FragmentSearchPhotosBinding
    private val pagingAdapter: PhotoPagingAdapter by lazy { PhotoPagingAdapter() }
    private val viewModel by activityViewModels<PhotosViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingState(false)
        showEmptyState(true)
        changeSearchViewPlate()
        binding.imagesRv.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PhotoLoadingAdapter { pagingAdapter.retry() },
            footer = PhotoLoadingAdapter { pagingAdapter.retry() }
        )

        viewModel.pagingSearchNews.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow
                .distinctUntilChangedBy {
                    it.refresh
                }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.imagesRv.scrollToPosition(0) }
        }

        pagingAdapter.addLoadStateListener { loadstate ->
            when (loadstate.source.refresh) {
                is LoadState.Loading -> {
                    showEmptyState(false)
                    showLoadingState(true)
                }
                is LoadState.Error -> {
                    showEmptyState(true)
                    showLoadingState(false)
                }
                is LoadState.NotLoading -> {
                    showEmptyState(true)
                    showLoadingState(false)
                }
            }

            pagingAdapter.adapterClickListener { imageBody ->
                val route = SearchPhotosDirections.actionSearchPhotosToViewPhoto(imageBody)
                findNavController().navigate(route)
            }

            binding.searchBar.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null)
                        viewModel.querySearch(query)
                    binding.searchBar.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            binding.backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }
    }

    private fun changeSearchViewPlate(){
        val searchPlate = binding.searchBar.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.transparent_background)
    }

    private fun showLoadingState(state:Boolean) {
        binding.progressBar.isVisible = state
    }
     private fun showEmptyState(state:Boolean) {
        binding.emptyStateTv.isVisible = state
    }

}