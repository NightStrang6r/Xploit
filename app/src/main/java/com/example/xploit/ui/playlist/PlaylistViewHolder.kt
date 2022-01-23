package com.example.xploit.ui.playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R

class PlaylistViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(item: PlaylistModel)  {
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val name2 = itemView.findViewById<TextView>(R.id.tv_name2)
        val cover = itemView.findViewById<ImageView>(R.id.iv_logo)

        name.text = item.name
        name2?.text = "Плейлист"
        cover.setImageResource(R.drawable.vk_cover)
    }
}