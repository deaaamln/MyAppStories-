package com.dea.myappstories.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token:String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoryResponses>

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token:String,
        @Query("location") location: Int = 1
    ) : Call<StoryResponses>

    @Multipart
    @POST("stories")
    fun doUploadStory(
        @Header("Authorization") token:String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}