package com.sparklead.evocharge.ui.states

import com.sparklead.evocharge.models.ChargingStationResponse

sealed class MapDetailUiState {

    object Loading : MapDetailUiState()

    data class Success(val list: ArrayList<ChargingStationResponse>) : MapDetailUiState()

    data class Error(val message: String) : MapDetailUiState()
}