package com.dea.myappstories.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.utils.Event

class RegisterViewModel(private val repo: StoryRepository) : ViewModel() {
    val toastText: LiveData<Event<String>> = repo.toastText

    fun doRegister(name: String, email: String, password: String) = repo.postRegister(name, email, password)

}