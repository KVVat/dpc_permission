package com.android.certification.niap.permission.dpctester.data

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

data class LogBox(
    var id: Long = Random.nextLong(),
    var name: String? ="none",
    var description: String?="none",
    var type: String? = "none",
    var childs: MutableList<LogBox> = mutableListOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        //For the child the lists should be blank
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LogBox> {
        override fun createFromParcel(parcel: Parcel): LogBox {
            return LogBox(parcel)
        }

        override fun newArray(size: Int): Array<LogBox?> {
            return arrayOfNulls(size)
        }
    }

}