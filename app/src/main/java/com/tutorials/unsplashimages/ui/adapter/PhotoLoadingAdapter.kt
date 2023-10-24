package com.tutorials.unsplashimages.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorials.unsplashimages.R
import com.tutorials.unsplashimages.databinding.PhotoLoadStateBinding

class PhotoLoadingAdapter (private val onRetryClicked:()->Unit): LoadStateAdapter<PhotoLoadingAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = PhotoLoadStateBinding.bind(view)
        init {
           binding.retryBtn.setOnClickListener{
                onRetryClicked.invoke()
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorText.text = "${loadState.error.localizedMessage}: Please check your network connection"
            }

            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                errorText.isVisible = loadState is LoadState.Error
                retryBtn.isVisible = loadState is LoadState.Error

            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_load_state, parent, false)
        return ViewHolder(view)
    }

}