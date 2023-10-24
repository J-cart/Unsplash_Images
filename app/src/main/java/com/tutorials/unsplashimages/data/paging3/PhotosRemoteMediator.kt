package com.tutorials.unsplashimages.data.paging3

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tutorials.unsplashimages.data.local.PhotoDatabase
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.data.model.RemoteKey
import com.tutorials.unsplashimages.data.remote.PhotosApiService

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val api: PhotosApiService,
    private val db: PhotoDatabase
) : RemoteMediator<Int, ImageBody>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageBody>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.getAllPhotos(page = currentPage)
            val endOfPaginationReached = response.body()?.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached!!) null else currentPage + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getAppDao().deleteAll()
                    db.getRemoteKeysDao().deleteAllKeys()
                }
                val keys = response.body()?.map { unsplashImage ->
                    RemoteKey(
                        id = unsplashImage.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }
                db.getRemoteKeysDao().insertAllKeys(remoteKey = keys!!)
                db.getAppDao().insertAll(images = response.body()!!)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageBody>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.getRemoteKeysDao().getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ImageBody>
    ): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                db.getRemoteKeysDao().getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ImageBody>
    ): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                db.getRemoteKeysDao().getRemoteKeys(id = unsplashImage.id)
            }
    }


}

