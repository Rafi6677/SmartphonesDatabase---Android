package com.example.smartphonesdatabase.models

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize

@Parcelize
class Smartphone(val id: String, val brand: String, val model: String, val systemVersion: String, val website: String) : Parcelable {
    constructor() : this("", "", "", "", "")
}