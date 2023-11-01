package com.sparklead.evocharge.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparklead.evocharge.ui.repositories.ChargingStationRepository
import com.sparklead.evocharge.ui.states.MapDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapDetailViewModel @Inject constructor(private val repository: ChargingStationRepository) :
    ViewModel() {

    private var _mapDetailUiState = MutableStateFlow<MapDetailUiState>(MapDetailUiState.Loading)
    val mapDetailUiState: StateFlow<MapDetailUiState> = _mapDetailUiState

    fun getChargingList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getChargingStationList()
                .catch {
                    _mapDetailUiState.value = MapDetailUiState.Error(it.message.toString())
                }
                .collect {
                    _mapDetailUiState.value = MapDetailUiState.Success(it)
                }
        }
    }
}