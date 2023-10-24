package com.tutorials.unsplashimages.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tutorials.unsplashimages.ClassTypeConverter
import com.tutorials.unsplashimages.model.ImageBody
import com.tutorials.unsplashimages.model.RemoteKey

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