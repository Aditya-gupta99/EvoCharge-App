package com.sparklead.evocharge.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparklead.evocharge.ui.repositories.SignInRepository
import com.sparklead.evocharge.ui.states.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val repository: SignInRepository) : ViewModel() {

    private val _signInUiState = MutableStateFlow<SignInUiState>(SignInUiState.Empty)
    val signInUiState : StateFlow<SignInUiState> = _signInUiState

    fun signInUser(email : String,password : String) {
        _signInUiState.value = SignInUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.signInUser(email,password)
                .catch {
                    _signInUiState.value = SignInUiState.Error(it.message.toString())
                }
                .collect{
                    _signInUiState.value = SignInUiState.Success(it.user)
                }
        }
    }
}