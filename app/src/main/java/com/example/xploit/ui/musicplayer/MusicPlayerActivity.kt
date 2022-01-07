package com.example.xploit.ui.musicplayer


import android.R.attr
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.xploit.R
import com.example.xploit.api.BASE_URL
import com.example.xploit.api.CoverUrl
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.databinding.ActivityMusicPlayerBinding
import com.example.xploit.ui.track.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.drawable.Drawable


import android.graphics.Bitmap

import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target


object MySingleton {
    var TrackData: Array<MusicRepository.Track>? = null
}

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

        val context = this

        val extraData = intent.getParcelableExtra<MusicModelIntent>(KEY_DATA_music_list)
        val name = extraData?.name
        val artist = extraData?.artist
        val duration = extraData?.playTime
        val cover = extraData?.imgCover
        val url = extraData?.url
        val urlType = extraData?.urlType

        var coverBitmap: Int = R.drawable.track_cover
        var playFlag = false;

        binding.tvTrackName.text = name
        binding.tvTrackArtist.text = artist

        binding.btBack.setOnClickListener {
            finish()
        }

        val urlFinal = if(urlType == 1) {
            Uri.parse("${BASE_URL}get/?id=${url?.substring(12)}")
        } else {
            Uri.parse("${BASE_URL}getByHash/?hash=${url?.substring(14)}")
        }
        MySingleton.TrackData = arrayOf(
            MusicRepository.Track(
                name!!,
                artist!!,
                coverBitmap,
                urlFinal,
                (3 * 60 + 41) * 1000
            )
        )

        fun setCoverUrl(query: String) : String? {
            var res : String? = null
            RetrofitInstance.api.getCoverUrl(query).enqueue(object : Callback<CoverUrl> {
                override fun onResponse(call: Call<CoverUrl>, response: Response<CoverUrl>) {
                    if(response.isSuccessful){
                        Log.d("devlog", "${response.body()?.url}")
                        if (response.body()?.url != null && !response.body()?.url?.isEmpty()!!)
                            Picasso.with(context)
                                .load(response.body()?.url)
                                .placeholder(R.drawable.track_cover)
                                .into(binding.ivTrackCover)
                        else
                            Picasso.with(context)
                                .load(R.drawable.track_cover)
                                .placeholder(R.drawable.track_cover)
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

        fun getCoverBitmap() {
            Picasso.with(this).load(cover).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom?) {
                    //coverBitmap = BitmapDrawable(resources, bitmap)
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }

        setCoverUrl("${name} ${artist}")

        //

        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                if (state == null) return
                val playing = state.state == PlaybackStateCompat.STATE_PLAYING
                //binding.btTrackPlay.isEnabled = !playing
                //binding.btTrackPause.isEnabled = playing
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
            if(!playFlag) {
                playFlag = true
                binding.btTrackPlay.setImageResource(R.drawable.ic_pause)
                mediaController?.transportControls?.play()
            } else {
                playFlag = false
                binding.btTrackPlay.setImageResource(R.drawable.ic_play)
                mediaController?.transportControls?.pause()
            }
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