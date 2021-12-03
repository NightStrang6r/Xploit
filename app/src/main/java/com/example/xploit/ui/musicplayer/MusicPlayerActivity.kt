package com.example.xploit.ui.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.xploit.R
import com.example.xploit.databinding.ActivityMainBinding
import com.example.xploit.databinding.ActivityMusicPlayerBinding
import com.example.xploit.ui.track.KEY_DATA_artist
import com.example.xploit.ui.track.KEY_DATA_cover
import com.example.xploit.ui.track.KEY_DATA_duration
import com.example.xploit.ui.track.KEY_DATA_name
import com.squareup.picasso.Picasso

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
        Picasso.with(this)
            .load(cover)
            .into(binding.ivTrackCover)
    }
}