package com.sparklead.evocharge.service

import com.sparklead.evocharge.models.ChargingStationResponse

interface ChargingStationService {

    suspend fun getChargingStationList(): ArrayList<ChargingStationResponse>

}