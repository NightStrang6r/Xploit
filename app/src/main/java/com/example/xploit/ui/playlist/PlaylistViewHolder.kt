package com.example.xploit.ui.playlist

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.ui.library.PlaylistActivity
import com.example.xploit.ui.musicplayer.MusicPlayerActivity
import com.example.xploit.ui.track.KEY_DATA_music_list
import com.example.xploit.ui.track.MusicModelIntent

const val KEY_DATA_playlist = "data_playlist"

class PlaylistViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(item: PlaylistModel)  {
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val name2 = itemView.findViewById<TextView>(R.id.tv_name2)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)
        val group = itemView.findViewById<ConstraintLayout>(R.id.cl_group)

        name.text = item.name
        name2?.text = "Плейлист"
        cover.setImageResource(R.drawable.vk_cover)

        group.setOnClickListener {
            val intent = Intent(itemView.context, PlaylistActivity::class.java)
            intent.putExtra(KEY_DATA_playlist, PlaylistModelIntent(
                item.name,
                item.imgCover,
                1
            ))

            ContextCompat.startActivity(itemView.context, intent, null)
        }
    }
}