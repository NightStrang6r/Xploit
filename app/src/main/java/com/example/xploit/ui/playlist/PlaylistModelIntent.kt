package com.example.xploit.ui.playlist

import android.os.Parcel
import android.os.Parcelable

class PlaylistModelIntent(
    val name : String?,
    val imgCover: String?,
    val isVk : Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(imgCover)
        parcel.writeInt(isVk)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistModelIntent> {
        override fun createFromParcel(parcel: Parcel): PlaylistModelIntent {
            return PlaylistModelIntent(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistModelIntent?> {
            return arrayOfNulls(size)
        }
    }
}