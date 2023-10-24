package com.tutorials.unsplashimages.arch

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.tutorials.unsplashimages.arch.paging3.PhotosRemoteMediator
import com.tutorials.unsplashimages.arch.paging3.SearchNewsPagingSource
import com.tutorials.unsplashimages.db.PhotoDatabase
import com.tutorials.unsplashimages.db.PhotosApiService
import com.tutorials.unsplashimages.model.ImageBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoApi: PhotosApiService, val db: PhotoDatabase) :
    PhotosRepository {

    override fun getPagingSearchImages(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SearchNewsPagingSource(photoApi, query) }
        ).liveData


    @OptIn(ExperimentalPagingApi::class)
    override fun getMediatorPagingImages(): LiveData<PagingData<ImageBody>> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = false),
            remoteMediator = PhotosRemoteMediator(photoApi, db),
            pagingSourceFactory = { db.getAppDao().getAllMediatorImages() }
        ).liveData
    }

    override suspend fun getAllItemsCount(): Int = db.getAppDao().getAllItemCount()

    //////////////////////////////////////////////////////////////////////////////////////

    override fun getAllSavedImages(): Flow<List<ImageBody>> =
        db.getSavedPhotosDao().getAllSavedImages()

    override suspend fun deleteSavedImage(image: ImageBody) {
        db.getSavedPhotosDao().deleteSavedImage(image)
    }

    override suspend fun saveImage(image: ImageBody) {
        db.getSavedPhotosDao().insertImage(image)
    }

    override suspend fun checkIfSavedExist(data: String): Int =
        db.getSavedPhotosDao().checkIfExists(data)

    override suspend fun deleteAllSaved() = db.getSavedPhotosDao().deleteAll()
}