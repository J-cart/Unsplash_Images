package com.tutorials.unsplashimages
sealed class PhotoEvent {
    object Successful : PhotoEvent()
    object Failure : PhotoEvent()
}
