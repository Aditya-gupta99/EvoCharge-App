package com.sparklead.evocharge.models

import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationResponse(
    val chargingType: String,

    val closingTime: String,

    val completeAddress: String,

    val images: List<String>,

    val latitude: Double,

    val longitude : Double,

    val location : String,

    val openingTime: String,

    val stationId: Int,

    val stationName: String
)