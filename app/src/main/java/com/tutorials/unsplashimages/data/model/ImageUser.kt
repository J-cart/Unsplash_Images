package com.tutorials.unsplashimages.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ImageUser(
    val username:String
): Parcelable