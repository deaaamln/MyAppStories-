package com.dea.myappstories.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.model.UserModel
import com.dea.myappstories.service.ListStoryItem
import com.dea.myappstories.utils.Event
import kotlinx.coroutines.launch

class MainViewModel(private val repo: StoryRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repo.isLoading
    val toastText: LiveData<Event<String>> = repo.toastText

    val getListStories: LiveData<PagingData<ListStoryItem>> =
        repo.getStories().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}