package com.tutorials.unsplashimages.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = "Images")
@Parcelize
data class ImageBody(
    @PrimaryKey(autoGenerate = true)
    val key:Int = 0,
    @SerializedName("id")
    val id:String,
    @SerializedName("urls")
    val urls: ImageUrl,
):Parcelable