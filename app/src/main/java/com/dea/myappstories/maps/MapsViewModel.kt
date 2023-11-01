package com.dea.myappstories.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.model.UserModel
import com.dea.myappstories.service.StoryResponses

class MapsViewModel(private val repo: StoryRepository) : ViewModel() {

    val listStoryLocation: LiveData<StoryResponses> = repo.listStoryLocation

    fun getStoriesWithLocation(token: String) = repo.getStoriesWithLocation(token)

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }
}