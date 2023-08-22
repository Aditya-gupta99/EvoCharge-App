package com.sparklead.evocharge.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargingStation(
    val name: String,
    val location: String,
    val available : Boolean
) : Parcelable
