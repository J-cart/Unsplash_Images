package com.tutorials.unsplashimages.db

import com.tutorials.unsplashimages.model.ImageBody
import com.tutorials.unsplashimages.model.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApiService {

    @GET("/photos")
    suspend fun getAllPhotos(
        @Query("page")
        page: Int,
        @Query("per_page")
        perPage: Int = 20
    ): Response<List<ImageBody>>

    @GET("search/photos")
    suspend fun searchPhoto(
        @Query("query")
        query: String,
        @Query("page")
        page: Int,
        @Query("per_page")
        perPage: Int = 20
    ): Response<ImageResponse>
}