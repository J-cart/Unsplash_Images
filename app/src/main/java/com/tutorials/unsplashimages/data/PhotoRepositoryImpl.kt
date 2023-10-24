package com.tutorials.unsplashimages.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.tutorials.unsplashimages.data.local.PhotoDatabase
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.data.paging3.PhotosRemoteMediator
import com.tutorials.unsplashimages.data.paging3.SearchPhotoPagingSource
import com.tutorials.unsplashimages.data.remote.PhotosApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoApi: PhotosApiService, val db: PhotoDatabase) :
    PhotosRepository {

    override fun getPagingSearchImages(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SearchPhotoPagingSource(photoApi, query) }
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

}