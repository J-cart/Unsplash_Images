package com.tutorials.unsplashimages.arch.paging3

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tutorials.unsplashimages.db.PhotoDatabase
import com.tutorials.unsplashimages.db.PhotosApiService
import com.tutorials.unsplashimages.model.ImageBody
import com.tutorials.unsplashimages.model.RemoteKey
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1


@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val api: PhotosApiService,
    private val db: PhotoDatabase
) : RemoteMediator<Int, ImageBody>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageBody>
    ): MediatorResult {

        val page =
            when (val pageKeyData = getKeyPageData(loadType, state)) {
                is MediatorResult.Success -> return pageKeyData
                else -> pageKeyData as Int
            }

        return try {
            val response = api.getAllPhotos(page = page)

            var mainResult = emptyList<ImageBody>()
            val result = response.body()
            result?.let {
                mainResult = it
            }
            Log.d("JOE", "RemoteMediator<main result>:${response.body()} .. ")
            val isEndOfList = mainResult.isEmpty() //.size < state.config.pageSize

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getAppDao().deleteAll()
                    db.getRemoteKeysDao().deleteAllKeys()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1

                val keys = mainResult.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.getRemoteKeysDao().insertAllKeys(keys)
                db.getAppDao().insertAll(mainResult)

            }
            Log.d("PAGINGSOURCEIO", "we fetched the data alright....")
            if(mainResult.isEmpty()){
                MediatorResult.Error(Exception("Unable to fetch photos"))
            }else{
                MediatorResult.Success(endOfPaginationReached = isEndOfList)
            }

        } catch (e: IOException) {
            Log.d("PAGINGSOURCEIO", "$e")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.d("PAGINGSOURCEIO", "$e")
            MediatorResult.Error(e)
        }catch (e:Exception) {
            Log.d("PAGINGSOURCEIO", "$e")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ImageBody>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosest(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey =
                    remoteKeys?.prevKey
                        ?: MediatorResult.Success(endOfPaginationReached = true)//
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey =
                    remoteKeys?.nextKey
                        ?: MediatorResult.Success(endOfPaginationReached = true)//
                nextKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ImageBody>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { image ->
                    db.getRemoteKeysDao().getRemoteKeys(image.id)
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ImageBody>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { image ->
                db.getRemoteKeysDao().getRemoteKeys(image.id)
            }
    }

    private suspend fun getRemoteKeyClosest(state: PagingState<Int, ImageBody>): RemoteKey? {
        return state.anchorPosition
            ?.let { position ->
                state.closestItemToPosition(position)?.id
                    ?.let {
                        db.getRemoteKeysDao().getRemoteKeys(it)
                    }
            }
    }


}

