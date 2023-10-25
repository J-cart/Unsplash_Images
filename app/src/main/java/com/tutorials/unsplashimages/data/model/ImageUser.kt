package com.tutorials.unsplashimages.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ImageUser(
    @SerializedName("id")
    val id:String,
    @SerializedName("username")
    val username:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("portfolio_url")
    val portfolio_url:String?,
): Parcelable