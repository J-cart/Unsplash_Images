package com.tutorials.unsplashimages.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUrl (
    val raw:String,
    val regular:String
): Parcelable
