package com.tutorials.unsplashimages.data.model

data class ImageResponse(
    val total:Int,
    val total_pages:Int,
    val  results:List<ImageBody>
)
