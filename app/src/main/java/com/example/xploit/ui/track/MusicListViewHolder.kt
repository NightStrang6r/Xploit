package com.example.xploit.ui.track;

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.squareup.picasso.Picasso
import android.content.Intent
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.xploit.ui.musicplayer.MusicPlayerActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.xploit.api.BASE_URL
import java.net.URLEncoder

const val KEY_DATA_music_list = "data_music_list"

class MusicListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(item: MusicModel) {
        val group = itemView.findViewById<ConstraintLayout>(R.id.cl_group)
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val artist = itemView.findViewById<TextView>(R.id.tv_artist)
        val duration = itemView.findViewById<TextView>(R.id.tv_playTime)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)

        val durationVal = item.playTime.substring(3)
        val imgCover = URLEncoder.encode("${item.imgCover}", "utf-8")
        val readyImageCover = "${BASE_URL}img/?url=${imgCover}"

        name.text = item.name
        artist.text = item.artist
        duration.text = durationVal
        Picasso.with(itemView.context)
            .load(readyImageCover)
            .placeholder(R.drawable.track_cover)
            .into(cover)

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
            startActivity(itemView.context, intent, null)
        }
    }
}