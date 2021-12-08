package com.example.xploit.ui.musicplayer

import android.net.Uri
import com.example.xploit.R

internal class MusicRepository {
    private val data = arrayOf(
        Track("Never Give Up",
            "NEFFEX",
            R.drawable.track_cover,
            Uri.parse("https://xploit.leoitdev.ru/get/?id=282884077_456239473"),
            (3 * 60 + 41) * 1000))
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

    internal class Track(
        val title: String,
        val artist: String,
        val bitmapResId: Int,
        val uri: Uri,
        val duration: Long, // in ms
    )
}