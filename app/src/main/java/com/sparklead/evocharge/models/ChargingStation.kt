package com.sparklead.evocharge.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargingStation(
    val name: String,

    val location: String,

    val available : Boolean,

    val distance : String,

    val openingTime : String,

    val closingTime : String,

    val completeAddress : String,

    val chargeType : String,

    val images : List<String>,

    val latitude : Double,

    val longitude : Double
) : Parcelable
