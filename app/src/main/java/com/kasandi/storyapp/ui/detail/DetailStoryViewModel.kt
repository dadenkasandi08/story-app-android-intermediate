package com.kasandi.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasandi.storyapp.data.StoryRepository
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun setDetailStory(id: String) = viewModelScope.launch {
        storyRepository.setDetailStory(id)
    }

    fun getDetailStoryResponse() = storyRepository.getDetailStoryResponse()

}