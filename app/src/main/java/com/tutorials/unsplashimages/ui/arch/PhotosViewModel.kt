package com.tutorials.unsplashimages.ui.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tutorials.unsplashimages.data.PhotosRepository
import com.tutorials.unsplashimages.data.model.ImageBody
import com.tutorials.unsplashimages.util.PhotoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotosRepository
) : ViewModel() {

    var homeFirstRun = MutableStateFlow(true)
        private set

    private val _photoEvent = Channel<PhotoEvent>()
    val photoEvent = _photoEvent.receiveAsFlow()

    fun checkSizeFromDB() {
        viewModelScope.launch {
            val count = repository.getAllItemsCount()
            if (count <= 0) {
                _photoEvent.send(PhotoEvent.Failure)
            } else {
                _photoEvent.send(PhotoEvent.Successful)
            }
        }

    }

    //PAGING
    private val searchQuery = MutableLiveData("")

    fun querySearch(query: String) {
        searchQuery.value = query
    }

    val pagingSearchPhotos = searchQuery.switchMap { query ->
        repository.getPagingSearchImages(query).cachedIn(viewModelScope)
    }

    var remoteMediatorPhotos = MutableStateFlow<Flow<PagingData<ImageBody>>>(flowOf(PagingData.empty()))
        private set


    fun getRemoteMediatorPhotos() {
        remoteMediatorPhotos.value = repository.getMediatorPagingImages().asFlow()
    }

    fun toggleHomeFirstRun(value: Boolean) {
        homeFirstRun.value = value
    }

}
