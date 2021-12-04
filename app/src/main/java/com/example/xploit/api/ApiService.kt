package com.example.xploit.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://xploit.leoitdev.ru/"

interface ApiService {
    @GET("getCatalog/?id=popular")
    fun getPopularPlaylist() : Call<ApiResp>
}

object RetrofitInstance {
    private val client = OkHttpClient.Builder().build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

data class CreateCalcRequest(
    val x: String
)

data class Solution(
    @SerializedName("x")
    val x: String,
    val result: String
)

data class ApiResp (
    val error: Boolean,
    @SerializedName("error_message")
    val errorMessage: String,
    @SerializedName("list_type")
    val listType: String,
    val items: List<Track>
)

data class Track (
    val url: String,
    val title: String,
    val artist: String,
    val duration: String,
    val image: String
)