package com.tutorials.unsplashimages.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tutorials.unsplashimages.R
import com.tutorials.unsplashimages.databinding.FragmentSearchPhotosBinding
import com.tutorials.unsplashimages.databinding.LoadingLayoutBinding
import com.tutorials.unsplashimages.ui.adapter.PhotoLoadingAdapter
import com.tutorials.unsplashimages.ui.adapter.PhotoPagingAdapter
import com.tutorials.unsplashimages.ui.arch.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class SearchPhotos : Fragment() {
    private lateinit var binding: FragmentSearchPhotosBinding
    private val pagingAdapter: PhotoPagingAdapter by lazy { PhotoPagingAdapter() }
    private val viewModel by activityViewModels<PhotosViewModel>()
    private lateinit var mContainer: ViewGroup
    private lateinit var loadingDialog:AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchPhotosBinding.inflate(inflater, container, false)
        mContainer = container!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = loadingDialog()
        toggleUIMode()
        showEmptyState(pagingAdapter.itemCount==0, "Search Wallpaper")
        changeSearchViewPlate()
        binding.imagesRv.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PhotoLoadingAdapter { pagingAdapter.retry() },
            footer = PhotoLoadingAdapter { pagingAdapter.retry() }
        )

        viewModel.pagingSearchPhotos.observe(viewLifecycleOwner) { pagingData ->
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            showEmptyState(pagingAdapter.itemCount==0, "Search Wallpaper")
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
                    loadingDialog.show()
                }

                is LoadState.Error -> {
                    showEmptyState(pagingAdapter.itemCount==0, "Search Wallpaper")
                    showErrorDialog()
                    loadingDialog.dismiss()

                }

                is LoadState.NotLoading -> {
                    loadingDialog.dismiss()
                    binding.imagesRv.isVisible = pagingAdapter.itemCount >= 0
                    showEmptyState(pagingAdapter.itemCount == 0, "Search Wallpaper")
                }
            }

            pagingAdapter.adapterClickListener { imageBody ->
                val route =
                    SearchPhotosDirections.actionSearchPhotosToViewPhoto(
                        imageBody
                    )
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

    private fun changeSearchViewPlate() {
        val searchPlate = binding.searchBar.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.transparent_background)
    }


    private fun showEmptyState(state: Boolean, text: String = "") {
        binding.emptyStateTv.isVisible = state
        binding.emptyStateTv.text = text
    }

    private fun loadingDialog(): AlertDialog {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.loading_layout, mContainer, false)
        val binding = LoadingLayoutBinding.bind(dialogView)
        val newDialog = MaterialAlertDialogBuilder(requireContext()).create()
        if (dialogView.parent != null) {
            (dialogView.parent as ViewGroup).removeView(binding.root)
        }
        newDialog.setView(binding.root)
        return newDialog
    }


    private fun showErrorDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage("Oops, Some error occurred or there is no result fro your query, Retry.")
            setTitle("Unsplash Images")
            setPositiveButton("OK") { dialogInterface, int ->
                dialogInterface.dismiss()
            }
            create()
        }
        dialogBuilder.show()

    }

    private fun toggleUIMode(){
        val search = binding.searchBar
        val searchEditText =
            search.findViewById(androidx.appcompat.R.id.search_src_text) as EditText

        val blackColor = ContextCompat.getColor(
            requireContext(),
            R.color.black
        )
        val greyColor = ContextCompat.getColor(
            requireContext(),
            R.color.grey_4
        )

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                searchEditText.setHintTextColor(blackColor)
                searchEditText.setTextColor(blackColor)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                searchEditText.setHintTextColor(greyColor)
                searchEditText.setTextColor(blackColor)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> searchEditText.setHintTextColor(blackColor)
        }
    }
}
