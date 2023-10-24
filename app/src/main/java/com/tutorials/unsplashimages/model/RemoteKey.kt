package com.tutorials.unsplashimages.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos_remote_keys")
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id:String,
    val prevKey:Int?,
    val nextKey:Int?
) {
}