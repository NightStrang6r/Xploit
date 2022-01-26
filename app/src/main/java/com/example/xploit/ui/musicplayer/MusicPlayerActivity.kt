package com.example.xploit.ui.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.xploit.R
import com.example.xploit.api.CoverUrl
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.databinding.ActivityMusicPlayerBinding
import com.example.xploit.ui.track.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.support.v4.media.MediaMetadataCompat
import android.widget.Toast
import java.util.*
import java.util.concurrent.TimeUnit

// Global settings and track lists
object MySingleton {
    var TrackData: Array<MusicRepository.Track>? = null
    var TrackDataSave: Array<MusicRepository.Track>? = null
    var NeedRefresh: Boolean = false
    var IsPlaying: Boolean = false
    var Repeat: Boolean = false
    var Shuffled: Boolean = false
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
        var isThisPlayedFlag = false
        var timer = Timer()
        val firstSong = MySingleton.TrackData?.get(0)

        binding.tvTrackName.text = firstSong?.title
        binding.tvTrackArtist.text = firstSong?.artist
        binding.tvSongCurrentProgress.text = "0:00"

        val songMinutes = (firstSong?.duration?.div(1000))?.div(60)
        val songSeconds = (firstSong?.duration?.div(1000))?.rem(60)
        binding.tvSongTotalTime.text = "${songMinutes}:${songSeconds}"
        var songCurMinutes = 0
        var songCurSeconds = 0

        if(MySingleton.Repeat)
            binding.btRepeatButton.setImageResource(R.drawable.ic_baseline_repeat_one)

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
                    }
                }

                override fun onFailure(call: Call<CoverUrl>, t: Throwable) { }
            })
            return res
        }

        fun updateTrackUI() {
            MySingleton.TrackData?.forEach {
                if(it.isNowPlaying) {
                    binding.tvTrackName.text = it.title
                    binding.tvTrackArtist.text = it.artist
                    setCoverUrl("${it.title} ${it.artist}")
                    return
                }
            }
        }

        fun setTimer(start: Boolean) {
            runOnUiThread(kotlinx.coroutines.Runnable {
                if(start) {
                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            songCurSeconds++
                            if(songCurSeconds > 60) {
                                songCurSeconds = 0
                                songCurMinutes++
                            }
                            if(songCurSeconds < 10)
                                binding.tvSongCurrentProgress.text = "${songCurMinutes}:0${songCurSeconds}"
                            else
                                binding.tvSongCurrentProgress.text = "${songCurMinutes}:${songCurSeconds}"
                        }
                    }, 0, 1000)
                } else {
                    timer.cancel()
                }
            })
        }

        setCoverUrl("${MySingleton.TrackData?.get(0)?.title} ${MySingleton.TrackData?.get(0)?.artist}")

        //

        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                if (state == null) return
                val playing = state.state == PlaybackStateCompat.STATE_PLAYING
                //binding.btTrackPlay.isEnabled = !playing
                //binding.btTrackPause.isEnabled = playing
                //stopButton.setEnabled(playing)
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                super.onMetadataChanged(metadata)
                updateTrackUI()
            }
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                playerServiceBinder = service as PlayerService.PlayerServiceBinder
                try {
                    mediaController = MediaControllerCompat(this@MusicPlayerActivity,
                        playerServiceBinder!!.mediaSessionToken)
                    mediaController?.registerCallback(callback as MediaControllerCompat.Callback)

                    // TODO: FIX NULL POINTER EXCEPTION
                    /*(callback as MediaControllerCompat.Callback)?.onPlaybackStateChanged(
                        mediaController?.playbackState)*/
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

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btTrackPlay.setOnClickListener {
            if(!isThisPlayedFlag) {
                if(MySingleton.IsPlaying) {
                    mediaController?.transportControls?.pause()
                }
                isThisPlayedFlag = true
                MySingleton.IsPlaying = true
                binding.btTrackPlay.setImageResource(R.drawable.ic_pause)
                //setTimer(true)
                mediaController?.transportControls?.play()
            } else {
                isThisPlayedFlag = false
                MySingleton.IsPlaying = false
                binding.btTrackPlay.setImageResource(R.drawable.ic_play)
                //setTimer(false)
                mediaController?.transportControls?.pause()
            }
        }

        binding.btTrackNext.setOnClickListener {
            mediaController?.transportControls?.skipToNext()
        }

        binding.btTrackPrevious.setOnClickListener {
            mediaController?.transportControls?.skipToPrevious()
        }

        binding.btRepeatButton.setOnClickListener {
            if(!MySingleton.Repeat) {
                MySingleton.Repeat = true
                binding.btRepeatButton.setImageResource(R.drawable.ic_baseline_repeat_one)
                msg("Повторение включено")
            } else {
                MySingleton.Repeat = false
                binding.btRepeatButton.setImageResource(R.drawable.ic_repeat)
                msg("Повторение выключено")
            }
        }

        binding.btShuffleButton.setOnClickListener {
            if(!MySingleton.Shuffled) {
                MySingleton.Shuffled = true
                val tempList = MySingleton.TrackData
                tempList?.shuffle()
                MySingleton.TrackData = tempList
                msg("Перемешивание включено")
            } else {
                MySingleton.Shuffled = false
                MySingleton.TrackData = MySingleton.TrackDataSave
                msg("Перемешивание выключено")
            }
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

    fun msg(msg: String) {
        val myToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        myToast.show()
    }
}