package com.sparklead.evocharge.ui.states

sealed class CompleteProfileUiState {

    object Empty : CompleteProfileUiState()

    object Loading : CompleteProfileUiState()

    data class Error(val message: String) : CompleteProfileUiState()

    data class Success(val message: String) : CompleteProfileUiState()

}