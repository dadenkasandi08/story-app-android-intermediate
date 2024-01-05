package com.kasandi.storyapp.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasandi.storyapp.data.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadStory(
        image: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody? = null,
        longitude: RequestBody? = null
    ) = viewModelScope.launch {
        storyRepository.uploadStory(image, description, latitude, longitude)
    }

    fun getUploadStoryResponse() = storyRepository.getUploadResponse()

}
