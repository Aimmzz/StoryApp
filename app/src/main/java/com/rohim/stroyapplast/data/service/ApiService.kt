package com.rohim.stroyapplast.data.service

import com.rohim.stroyapplast.data.request.LoginRequest
import com.rohim.stroyapplast.data.request.RegisterRequest
import com.rohim.stroyapplast.data.response.LoginResponse
import com.rohim.stroyapplast.data.response.RegisterResponse
import com.rohim.stroyapplast.data.response.StorysResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //untuk auth
    @POST("login")
    suspend fun login(
        @Body login: LoginRequest
    ): LoginResponse
//    @POST("login")
//    suspend fun login(
//        @Field("email")email:String,
//        @Field("password")password:String,
//    ):Response<LoginResponse>
    @POST("register")
    suspend fun register(
        @Body register: RegisterRequest
    ): RegisterResponse

    //untuk story
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StorysResponse

    @GET("stories")
    suspend fun getLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ) : StorysResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?=null,
        @Part("lon") lon: Float?=null,
    ): RegisterResponse
}