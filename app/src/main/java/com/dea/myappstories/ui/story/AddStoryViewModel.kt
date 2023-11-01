package com.dea.myappstories.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.model.UserModel
import com.dea.myappstories.service.AddStoryResponse
import com.dea.myappstories.utils.Event
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repo: StoryRepository) : ViewModel() {

    val uploadResponse: LiveData<AddStoryResponse> = repo.uploadResponse
    val isLoading: LiveData<Boolean> = repo.isLoading
    val toastText: LiveData<Event<String>> = repo.toastText

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repo.uploadStory(token, file, description)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }
}