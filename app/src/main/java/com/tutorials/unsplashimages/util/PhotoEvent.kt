package com.tutorials.unsplashimages.util
sealed class PhotoEvent {
    object Successful : PhotoEvent()
    object Failure : PhotoEvent()
}
