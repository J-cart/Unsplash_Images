package com.tutorials.unsplashimages.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorials.unsplashimages.data.local.dao.PhotosDao
import com.tutorials.unsplashimages.data.local.dao.RemoteKeyDao
import com.tutorials.unsplashimages.data.local.dao.SavedPhotoDao
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.data.model.RemoteKey
import com.tutorials.unsplashimages.util.ClassTypeConverter

@Database(
    entities = [ImageBody::class, RemoteKey::class],
    version = 1
)
@TypeConverters(ClassTypeConverter::class)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun getAppDao(): PhotosDao
    abstract fun getRemoteKeysDao(): RemoteKeyDao
    abstract fun getSavedPhotosDao(): SavedPhotoDao
}