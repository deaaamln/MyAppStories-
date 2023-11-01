package com.dea.myappstories.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dea.myappstories.service.*
import com.dea.myappstories.ui.story.StoryPagingSource
import com.dea.myappstories.utils.Event
import com.dea.myappstories.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: SessionPreferences
){
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _uploadResponse = MutableLiveData<AddStoryResponse>()
    val uploadResponse: LiveData<AddStoryResponse> = _uploadResponse

    private val _listStoryLocation = MutableLiveData<StoryResponses>()
    val listStoryLocation: LiveData<StoryResponses> = _listStoryLocation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun postRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.doRegister(name, email, password)
            val error = response.error
            if (!error){
                emit(Result.Success(response))
            }
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
            Log.d(TAG, "onFailure: ${e.message.toString()}")
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.doLogin(email, password)
            val error = response.error
            if (!error) {
                emit(Result.Success(response))
            }
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
            Log.d(TAG, "onFailure: ${e.message.toString()}")
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(pref, apiService)
            }
        ).liveData
    }


    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = apiService.doUploadStory(token, file, description)

        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _uploadResponse.value = response.body()
                    _toastText.value = Event(response.body()?.message.toString())
                } else {
                    _toastText.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.d("error upload", t.message.toString())
            }

        }
        )
    }

    fun getStoriesWithLocation(token: String) {
        _isLoading.value = true
        val client = apiService.getStoriesWithLocation(token)

        client.enqueue(object : Callback<StoryResponses> {
            override fun onResponse(
                call: Call<StoryResponses>,
                response: Response<StoryResponses>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listStoryLocation.value = response.body()

                    Log.d("getStory", "onResponse: ${response.body()}")
                    Log.d("getStoryDetail", "onResponse: ${response.body()?.listStory}")
                } else {
                    Log.e(
                        "getStory",
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<StoryResponses>, t: Throwable) {
                Log.e("getStory", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }

    suspend fun saveSession(session: UserModel) {
        pref.saveSession(session)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: SessionPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, preferences)
            }.also { instance = it }
    }
}