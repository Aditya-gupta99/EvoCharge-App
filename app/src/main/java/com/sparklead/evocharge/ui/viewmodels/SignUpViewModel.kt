package com.sparklead.evocharge.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparklead.evocharge.ui.repositories.SignUpRepository
import com.sparklead.evocharge.ui.states.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: SignUpRepository) : ViewModel() {

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Empty)
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState

    fun signUpUser(firstname: String, lastName: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpUiState.value = SignUpUiState.Loading
            repository.signUser(firstname, lastName, email, password)
                .catch {
                    _signUpUiState.value = SignUpUiState.Error(it.message.toString())
                    Log.e("error",it.message.toString())
            }.collect {
                _signUpUiState.value = SignUpUiState.Success(it.toString())
            }
        }
    }

}