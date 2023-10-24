package com.tutorials.unsplashimages.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.tutorials.unsplashimages.data.model.ImageBody

interface PhotosRepository {

    fun getPagingSearchImages(query: String):LiveData<PagingData<ImageBody>>
    fun getMediatorPagingImages():LiveData<PagingData<ImageBody>>
    suspend fun getAllItemsCount():Int
}