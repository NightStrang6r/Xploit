package com.example.xploit.ui.track;

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.squareup.picasso.Picasso
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.xploit.ui.musicplayer.MusicPlayerActivity
import androidx.core.content.ContextCompat.startActivity

const val KEY_DATA_name = "data_name"
const val KEY_DATA_artist = "data_artist"
const val KEY_DATA_duration = "data_duration"
const val KEY_DATA_cover = "data_cover"
const val KEY_DATA_url = "data_url"

class MusicListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(item: MusicModel) {
        val group = itemView.findViewById<ConstraintLayout>(R.id.cl_group)
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val artist = itemView.findViewById<TextView>(R.id.tv_artist)
        val duration = itemView.findViewById<TextView>(R.id.tv_playTime)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)

        val durationVal = item.playTime.substring(3)

        name.text = item.name
        artist.text = item.artist
        duration.text = durationVal
        Picasso.with(itemView.context)
            .load(item.imgCover)
            .into(cover)

        group.setOnClickListener {
            val intent = Intent(itemView.context, MusicPlayerActivity::class.java)
            intent.putExtra(KEY_DATA_name, item.name)
            intent.putExtra(KEY_DATA_artist, item.artist)
            intent.putExtra(KEY_DATA_duration, durationVal)
            intent.putExtra(KEY_DATA_cover, item.imgCover)
            intent.putExtra(KEY_DATA_url, item.url)
            startActivity(itemView.context, intent, null)
        }
    }
}