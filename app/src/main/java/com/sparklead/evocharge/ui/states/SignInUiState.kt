package com.sparklead.evocharge.ui.states

import com.sparklead.evocharge.models.User

sealed class SignInUiState {

    object Empty : SignInUiState()

    object Loading : SignInUiState()

    data class Success(val user : User) : SignInUiState()

    data class Error(val message : String) : SignInUiState()
}