package com.tutorials.unsplashimages.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorials.unsplashimages.model.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM photos_remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id:String): RemoteKey?

    @Query("DELETE FROM photos_remote_keys")
    suspend fun deleteAllKeys()
}