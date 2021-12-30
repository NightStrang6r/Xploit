package com.example.xploit.ui.track

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.ui.track.MusicModel

class MusicListAdapter (
    private val context: Context
) : RecyclerView.Adapter<MusicListViewHolder>() {
    private var list = mutableListOf<MusicModel>()

    fun setData(newList: List<MusicModel>){
        list = mutableListOf()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(newList: List<MusicModel>){
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.music_track, parent, false)
        return MusicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}