package com.tutorials.unsplashimages.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tutorials.unsplashimages.R
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.databinding.PhotoViewHolderBinding
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class PhotoPagingAdapter : PagingDataAdapter<ImageBody, PhotoPagingAdapter.ViewHolder>(diffObject) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = PhotoViewHolderBinding.bind(view)

        fun bind(image: ImageBody) {
            binding.apply {
                imageSrc.clipToOutline = true
                root.clipToOutline = true
                imageSrc.load(image.urls.raw) {
                    placeholder(R.drawable.loading_anim)
                    error(R.drawable.image_icon_small)
                }
                root.setOnClickListener {
                    listener?.let {
                        it(image)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        if (pos != null) {
            holder.bind(pos)
        }
    }


    companion object {
        val diffObject = object : DiffUtil.ItemCallback<ImageBody>() {
            override fun areItemsTheSame(oldItem: ImageBody, newItem: ImageBody): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ImageBody, newItem: ImageBody): Boolean {
                return oldItem.urls == newItem.urls
            }
        }
    }

    private var listener: ((ImageBody) -> Unit)? = null

    fun adapterClickListener(listener: (ImageBody) -> Unit) {
        this.listener = listener
    }


}