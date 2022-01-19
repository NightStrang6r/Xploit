package com.example.xploit.ui.track

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeResource
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.api.BASE_URL
import com.example.xploit.ui.musicplayer.MusicRepository
import com.example.xploit.ui.musicplayer.MySingleton
import com.example.xploit.ui.track.MusicModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class MusicListAdapter (
    private val context: Context
) : RecyclerView.Adapter<MusicListViewHolder>() {
    private var list = mutableListOf<MusicModel>()

    fun setData(newList: List<MusicModel>){
        list = mutableListOf()
        list.addAll(newList)

        var tempList = mutableListOf<MusicRepository.Track>()
        var coverBitmap: Bitmap = decodeResource(context.resources, R.drawable.track_cover)
        list.forEach { it ->
            val urlFinal = if(it.urlType == 1) {
                Uri.parse("${BASE_URL}get/?id=${it.url?.substring(12)}")
            } else {
                Uri.parse("${BASE_URL}getByHash/?hash=${it.url?.substring(14)}")
            }
            val playTimeMinutes = it.playTime?.substring(3, 4).toLong()
            val playTimeSeconds = it.playTime?.substring(5).toLong()
            val readyImgCoverUrl = "${BASE_URL}img/?url=${it.imgCover}"

            tempList.add(MusicRepository.Track(
                it.name,
                it.artist,
                coverBitmap,
                readyImgCoverUrl,
                urlFinal,
                (playTimeMinutes * 60 + playTimeSeconds) * 1000
            ))
        }
        MySingleton.TrackData = tempList.toTypedArray()

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