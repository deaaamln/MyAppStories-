package com.dea.myappstories.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.model.UserModel

class SplashScreenViewModel(private val repo: StoryRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }
}