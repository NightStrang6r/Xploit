package com.example.xploit.ui.musicplayer

/*import android.app.MediaRouteButton
import android.content.Context
import android.media.MediaDescription
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver


class MediaStyleHelper {
    fun from(context: Context?, mediaSession: MediaSession): NotificationCompat.Builder? {
        val controller: MediaController = mediaSession.controller
        val mediaMetadata: MediaMetadata? = controller.metadata
        val description: MediaDescription? = mediaMetadata?.description
        val builder = NotificationCompat.Builder(context!!)
        if (description != null) {
            builder
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())
                .setContentIntent(controller.getSessionActivity())
                .setDeleteIntent(
                    MediaRouteButton.buildMediaButtonPendingIntent(context,
                        PlaybackState.ACTION_STOP))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }
        return builder
    }
}*/