package com.tutorials.unsplashimages.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageLocation (
    @SerializedName("city")
    val name:String
): Parcelable