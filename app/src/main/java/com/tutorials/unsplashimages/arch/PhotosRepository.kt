package com.tutorials.unsplashimages.arch

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.tutorials.unsplashimages.model.ImageBody
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    fun getPagingSearchImages(query: String):LiveData<PagingData<ImageBody>>
    fun getMediatorPagingImages():LiveData<PagingData<ImageBody>>
    suspend fun getAllItemsCount():Int

    fun getAllSavedImages(): Flow<List<ImageBody>>
    suspend fun deleteSavedImage(image: ImageBody)
    suspend fun saveImage(image: ImageBody)
    suspend fun checkIfSavedExist(data:String):Int
    suspend fun deleteAllSaved()

}