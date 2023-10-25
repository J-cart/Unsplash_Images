package com.tutorials.unsplashimages.data.paging3

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.data.remote.PhotosApiService
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1
class SearchPhotoPagingSource(
    private val api: PhotosApiService,
    private val query:String
):PagingSource<Int, ImageBody>() {
    override fun getRefreshKey(state: PagingState<Int, ImageBody>): Int? {
        //check the paging3 library codelab for more info on this
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageBody> {
        val position = params.key?: STARTING_PAGE_INDEX
        return try {
            val response = api.searchPhoto(query = query, page = position)
            val result = response.body()
            var realResult = emptyList<ImageBody>()

            result?.let {
                realResult = it.results
            }
            LoadResult.Page(
                data = realResult,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position -1,
                nextKey = if (realResult.isEmpty()) null else position + 1
            )
        }catch (e: IOException){
            LoadResult.Error(e)

        }catch (e: HttpException){
            LoadResult.Error(e)
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}