package com.dea.myappstories.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dea.myappstories.model.StoryRepository
import com.dea.myappstories.model.UserModel
import com.dea.myappstories.utils.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: StoryRepository) : ViewModel() {

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun doLogin(email: String, password: String) = repo.postLogin(email, password)

    fun saveSession(session: UserModel) {
        viewModelScope.launch {
            repo.saveSession(session)
        }
    }

    fun login() {
        viewModelScope.launch {
            repo.login()
        }
    }
}