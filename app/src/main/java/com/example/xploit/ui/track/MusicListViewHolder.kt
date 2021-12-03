package com.example.xploit.ui.track;

import android.R.attr
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.ui.track.MusicModel
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext
import android.R.attr.radius
import android.view.RoundedCorner
import com.squareup.picasso.Transformation

class MusicListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(item: MusicModel) {
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val artist = itemView.findViewById<TextView>(R.id.tv_artist)
        val duration = itemView.findViewById<TextView>(R.id.tv_playTime)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)

        name.text = item.name
        artist.text = item.artist
        duration.text = item.playTime.substring(3)
        //cover.setImageResource(R.drawable.cover_warriors)
        Picasso.with(itemView.context)
            .load(item.imgCover)
            .into(cover)
    }
}