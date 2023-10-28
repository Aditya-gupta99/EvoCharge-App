package com.sparklead.evocharge.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparklead.evocharge.ui.repositories.ChargingStationRepository
import com.sparklead.evocharge.ui.states.ChargingStationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingListViewModel @Inject constructor(private val repository: ChargingStationRepository) :
    ViewModel() {

    private val _chargingUiState =
        MutableStateFlow<ChargingStationUiState>(ChargingStationUiState.Loading)
    val chargingUiState: StateFlow<ChargingStationUiState> = _chargingUiState

    fun getChargingList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getChargingStationList()
                .catch {
                    _chargingUiState.value = ChargingStationUiState.Error(it.message.toString())
                }
                .collect {
                    _chargingUiState.value = ChargingStationUiState.Success(it)
                }
        }
    }

}