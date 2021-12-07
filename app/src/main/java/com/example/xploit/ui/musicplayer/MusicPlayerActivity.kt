package com.example.xploit.ui.musicplayer

import android.app.DownloadManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.xploit.R
import com.example.xploit.api.ApiResp
import com.example.xploit.api.CoverUrl
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.databinding.ActivityMainBinding
import com.example.xploit.databinding.ActivityMusicPlayerBinding
import com.example.xploit.ui.track.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(KEY_DATA_name)
        val artist = intent.getStringExtra(KEY_DATA_artist)
        val duration = intent.getStringExtra(KEY_DATA_duration)
        val cover = intent.getStringExtra(KEY_DATA_cover)

        binding.tvTrackName.text = name
        binding.tvTrackArtist.text = artist

        val context = this

        fun setCoverUrl(query: String) : String? {
            var res : String? = null
            RetrofitInstance.api.getCoverUrl(query).enqueue(object : Callback<CoverUrl> {
                override fun onResponse(call: Call<CoverUrl>, response: Response<CoverUrl>) {
                    if(response.isSuccessful){
                        Picasso.with(context)
                            .load(response.body()?.url)
                            .into(binding.ivTrackCover)
                    } else {
                        val i = 0
                    }
                }

                override fun onFailure(call: Call<CoverUrl>, t: Throwable) {
                    val i = 0
                }
            })
            return res
        }

        setCoverUrl("${name} ${artist}")
    }
}