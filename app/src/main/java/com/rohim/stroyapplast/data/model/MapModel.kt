package com.rohim.stroyapplast.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapModel(
    val name: String,
    val latitude: Double,
    val longitude: Double
): Parcelable