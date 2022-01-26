package com.example.xploit.ui.library

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xploit.api.ApiResp
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.databinding.ActivityPlaylistBinding
import com.example.xploit.ui.playlist.KEY_DATA_playlist
import com.example.xploit.ui.playlist.PlaylistModelIntent
import com.example.xploit.ui.track.KEY_DATA_music_list
import com.example.xploit.ui.track.MusicListAdapter
import com.example.xploit.ui.track.MusicModel
import com.example.xploit.ui.track.MusicModelIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TrackList //
        val rvList = binding.rvList
        val pbListLoading = binding.pbListLoading
        val adapter = MusicListAdapter(this)
        val layoutManager = LinearLayoutManager(this)

        rvList.adapter = adapter
        rvList.layoutManager = layoutManager

        var trackList = mutableListOf<MusicModel>()
        val extraData = intent.getParcelableExtra<PlaylistModelIntent>(KEY_DATA_playlist)
        val isVk = extraData?.isVk

        fun getVkPlaylist() {
            pbListLoading.visibility = View.VISIBLE
            val sharedPref = this.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)
            var VkID = sharedPref?.getString("VkID", "0")
            RetrofitInstance.api.getVkPlaylist(VkID!!).enqueue(object : Callback<ApiResp> {
                override fun onResponse(call: Call<ApiResp>, response: Response<ApiResp>) {
                    if(response.isSuccessful){
                        response.body()?.items?.forEach { it ->
                            trackList.add(MusicModel(
                                it.title,
                                it.artist,
                                it.duration,
                                it.image,
                                it.url,
                                1))
                        }
                        adapter.setData(trackList)
                        pbListLoading.visibility = View.INVISIBLE
                    } else {
                        Msg("Не удалось получить список песен (code: resp).")
                        pbListLoading.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<ApiResp>, t: Throwable) {
                    Msg("Не удалось получить список песен (code: fail).")
                    pbListLoading.visibility = View.INVISIBLE
                }
            })
        }

        if(isVk == 1) {
            getVkPlaylist()
        }

        binding.btBack.setOnClickListener {
            finish()
        }
        // TrackList //
    }

    fun Msg(msg: String) {
        val myToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        myToast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}