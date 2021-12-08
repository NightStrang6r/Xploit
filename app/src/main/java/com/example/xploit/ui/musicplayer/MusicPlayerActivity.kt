package com.example.xploit.ui.musicplayer

import android.app.DownloadManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
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

    private var playerServiceBinder: PlayerService.PlayerServiceBinder? = null
    private var mediaController: MediaControllerCompat? = null
    private var callback: MediaControllerCompat.Callback? = null
    private var serviceConnection: ServiceConnection? = null

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

        //

        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                if (state == null) return
                val playing = state.state == PlaybackStateCompat.STATE_PLAYING
                binding.btTrackPlay.isEnabled = !playing
                binding.btTrackPause.isEnabled = playing
                //stopButton.setEnabled(playing)
            }
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                playerServiceBinder = service as PlayerService.PlayerServiceBinder
                try {
                    mediaController = MediaControllerCompat(this@MusicPlayerActivity,
                        playerServiceBinder!!.mediaSessionToken)
                    mediaController!!.registerCallback(callback as MediaControllerCompat.Callback)

                    // TODO: FIX NULL POINTER EXCEPTION
                    //(callback as MediaControllerCompat.Callback).onPlaybackStateChanged(
                        //mediaController!!.playbackState)
                } catch (e: RemoteException) {
                    mediaController = null
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                playerServiceBinder = null
                if (mediaController != null) {
                    mediaController!!.unregisterCallback(callback as MediaControllerCompat.Callback)
                    mediaController = null
                }
            }
        }

        bindService(Intent(this, PlayerService::class.java),
            serviceConnection as ServiceConnection, BIND_AUTO_CREATE)

        binding.btTrackPlay.setOnClickListener {
            mediaController?.transportControls?.play()
        }

        binding.btTrackPause.setOnClickListener {
            mediaController?.transportControls?.pause()
        }

        binding.btTrackNext.setOnClickListener {
            mediaController?.transportControls?.skipToNext()
        }

        binding.btTrackPrevious.setOnClickListener {
            mediaController?.transportControls?.skipToPrevious()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerServiceBinder = null
        if (mediaController != null) {
            mediaController!!.unregisterCallback(callback!!)
            mediaController = null
        }
        unbindService(serviceConnection!!)
    }
}