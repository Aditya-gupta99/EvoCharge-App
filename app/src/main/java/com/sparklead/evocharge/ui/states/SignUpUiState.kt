package com.sparklead.evocharge.ui.states

sealed class SignUpUiState {

    object Empty : SignUpUiState()

    object Loading : SignUpUiState()

    data class Error(val message : String) : SignUpUiState()

    data class Success(val message: String) : SignUpUiState()
}