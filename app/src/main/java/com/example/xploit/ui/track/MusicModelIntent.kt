package com.example.xploit.ui.track

import android.os.Parcel
import android.os.Parcelable

class MusicModelIntent(
    val name: String?,
    val artist: String?,
    val playTime: String?,
    val imgCover: String?,
    val url: String?,
    val urlType: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(artist)
        parcel.writeString(playTime)
        parcel.writeString(imgCover)
        parcel.writeString(url)
        parcel.writeInt(urlType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicModelIntent> {
        override fun createFromParcel(parcel: Parcel): MusicModelIntent {
            return MusicModelIntent(parcel)
        }

        override fun newArray(size: Int): Array<MusicModelIntent?> {
            return arrayOfNulls(size)
        }
    }
}