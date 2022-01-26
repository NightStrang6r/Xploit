package com.example.xploit.ui.track;

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.api.BASE_URL
import com.example.xploit.ui.musicplayer.MusicPlayerActivity
import com.example.xploit.ui.musicplayer.MusicRepository
import com.example.xploit.ui.musicplayer.MySingleton
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.net.URLEncoder
import java.util.*
import java.util.stream.IntStream

const val KEY_DATA_music_list = "data_music_list"

class MusicListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun bindData(item: MusicModel) {
        val group = itemView.findViewById<ConstraintLayout>(R.id.cl_group)
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val artist = itemView.findViewById<TextView>(R.id.tv_artist)
        val duration = itemView.findViewById<TextView>(R.id.tv_playTime)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)

        val durationVal = item.playTime.substring(3)
        val imgCover = URLEncoder.encode("${item.imgCover}", "utf-8")
        val readyImageCover = "${BASE_URL}img/?url=${imgCover}"

        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                MySingleton.TrackData?.forEach {
                    if(it.title == name.text)
                        it.bitmapResId = bitmap
                }
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }

        name.text = item.name
        artist.text = item.artist
        duration.text = durationVal
        // Для активити
        Picasso.with(itemView.context)
            .load(readyImageCover)
            .placeholder(R.drawable.track_cover)
            .into(cover)

        // Для PlayerService
        Picasso.with(itemView.context)
            .load(readyImageCover)
            .placeholder(R.drawable.track_cover)
            .into(target)

        group.setOnClickListener {
            val intent = Intent(itemView.context, MusicPlayerActivity::class.java)
            intent.putExtra(KEY_DATA_music_list, MusicModelIntent(
                item.name,
                item.artist,
                durationVal,
                item.imgCover,
                item.url,
                item.urlType
            ))

            var id = 0
            var counter = 0

            // Восстанавливаем копию списка песен
            MySingleton.TrackData = MySingleton.TrackDataSave

            MySingleton.TrackData?.forEach {
                if(it.title == item.name)
                    id = counter
                counter++
            }

            MySingleton.TrackData = shift(MySingleton.TrackData!!, id)
            MySingleton.NeedRefresh = true

            startActivity(itemView.context, intent, null)
        }
    }

    fun shift(array: Array<MusicRepository.Track>, shift: Int): Array<MusicRepository.Track> {
        val copy = array.copyOf(shift)
        System.arraycopy(array, shift, array, 0, array.size - shift)
        System.arraycopy(copy, 0, array, array.size - shift, shift)
        return array
    }
}