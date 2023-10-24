package com.tutorials.unsplashimages.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = "Images")
@Parcelize
data class ImageBody(
    @PrimaryKey
    val key:Int = 0,
    @SerializedName("id")
    val id:String,
   /* @SerializedName("location")
    val location:ImageLocation,*/
    @SerializedName("urls")
    val urls:ImageUrl,
//    val user:ImageUser,
//    @SerializedName("likes")
//    val likes:Int,
//    @SerializedName("downloads")
//    val downloads:Int
):Parcelable