package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.ChargingStationResponse
import kotlinx.coroutines.flow.Flow

interface ChargingStationRepository {

    fun getChargingStationList(): Flow<ArrayList<ChargingStationResponse>>

}