package com.tutorials.unsplashimages.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tutorials.unsplashimages.BASE_URL
import com.tutorials.unsplashimages.PHOTOS_DATABASE_NAME
import com.tutorials.unsplashimages.arch.PhotoRepositoryImpl
import com.tutorials.unsplashimages.arch.PhotosRepository
import com.tutorials.unsplashimages.db.PhotoDatabase
import com.tutorials.unsplashimages.db.PhotosApiService
import com.tutorials.unsplashimages.db.PhotosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            PhotoDatabase::class.java,
            PHOTOS_DATABASE_NAME
        ).build()


    @Singleton
    @Provides
    fun getMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun getOkhttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->

                val request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Client-ID 2TDGd6XXrdL_paXcQoP8jQu5HtvlN-AkUY5_bJuyekQ"//yGOXsLHI0BUw0QOY3S5l1wxKIAexQP5_eVnd8CsyyQs
                    ).build()
                chain.proceed(request)
            }
            .build()
        return client
    }

    @Singleton
    @Provides
    fun getRepository(api: PhotosApiService, db: PhotoDatabase): PhotosRepository =
        PhotoRepositoryImpl(api, db)

    @Singleton
    @Provides
    fun getDao(db: PhotoDatabase): PhotosDao = db.getAppDao()

    @Singleton
    @Provides
    fun getRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): PhotosApiService = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(PhotosApiService::class.java)



}