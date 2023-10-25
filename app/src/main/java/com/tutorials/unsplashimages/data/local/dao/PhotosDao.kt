package com.tutorials.unsplashimages.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorials.unsplashimages.data.model.ImageBody


@Dao
interface PhotosDao {

//PAGING
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageBody>)

    @Query("SELECT * FROM Images")
    fun getAllMediatorImages(): PagingSource<Int, ImageBody>

    @Query("DELETE FROM Images")
    suspend fun deleteAll()

    @Query("SELECT COUNT() FROM Images")
    suspend fun getAllItemCount():Int



}