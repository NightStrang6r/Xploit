package com.example.xploit.ui.musicplayer

import android.graphics.Bitmap
import android.net.Uri
import com.example.xploit.R

class MusicRepository(val data: Array<Track>) {
    private val maxIndex = data.size - 1
    private var currentItemIndex = 0

    val next: Track
        get() {
            if (currentItemIndex == maxIndex) currentItemIndex = 0 else currentItemIndex++
            return current
        }
    val previous: Track
        get() {
            if (currentItemIndex == 0) currentItemIndex = maxIndex else currentItemIndex--
            return current
        }
    val current: Track
        get() = data[currentItemIndex]

    data class Track(
        val title: String,
        val artist: String,
        var bitmapResId: Bitmap,
        val coverUrl: String,
        val uri: Uri,
        val duration: Long,
        var isNowPlaying: Boolean
    )
}

