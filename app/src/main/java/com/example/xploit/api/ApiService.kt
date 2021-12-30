package com.example.xploit.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.google.gson.GsonBuilder

import com.google.gson.Gson

const val BASE_URL = "https://xploit.leoitdev.ru/"

interface ApiService {
    @GET("getCatalog/?id=popular")
    fun getPopularPlaylist() : Call<ApiResp>

    @GET("getCoverUrl/")
    fun getCoverUrl(@Query("q") query: String) : Call<CoverUrl>

    @GET("search/")
    fun getTrackListBySearch(@Query("q") query: String) : Call<ApiResp>
}

object RetrofitInstance {
    private val client = OkHttpClient.Builder().build()
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

data class ApiResp(
    val error: Boolean,
    @SerializedName("error_message")
    val errorMessage: String,
    @SerializedName("list_type")
    val listType: String,
    val items: List<Track>,
)

data class Track(
    val url: String,
    val title: String,
    val artist: String,
    val duration: String,
    val image: String,
)

data class CoverUrl(
    val url: String,
)