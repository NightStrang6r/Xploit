package com.example.xploit.ui.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.ui.track.MusicModel

class PlaylistAdapter (
    private val context: Context
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    private var list = mutableListOf<PlaylistModel>()

    fun addData(newList: List<PlaylistModel>){
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.music_track, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}