package com.tutorials.unsplashimages.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ImageUser(
    val username:String
): Parcelable