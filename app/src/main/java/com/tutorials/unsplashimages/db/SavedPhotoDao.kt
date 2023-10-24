package com.tutorials.unsplashimages.db

import androidx.room.*
import com.tutorials.unsplashimages.model.ImageBody
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPhotoDao {


    @Query("SELECT * FROM Images")
    fun getAllSavedImages(): Flow<List<ImageBody>>

    @Query("SELECT COUNT() FROM images WHERE id =:data")
    suspend fun checkIfExists(data:String):Int

    @Delete
    suspend fun deleteSavedImage(image: ImageBody)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageBody)

    @Query("DELETE FROM Images")
    suspend fun deleteAll()
}