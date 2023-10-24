package com.tutorials.unsplashimages.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageLocation (
    @SerializedName("city")
    val name:String
): Parcelable