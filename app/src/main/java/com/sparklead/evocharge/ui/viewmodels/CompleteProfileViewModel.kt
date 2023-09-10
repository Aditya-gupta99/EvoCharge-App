package com.sparklead.evocharge.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparklead.evocharge.models.User
import com.sparklead.evocharge.ui.repositories.CompleteProfileRepository
import com.sparklead.evocharge.ui.states.CompleteProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteProfileViewModel @Inject constructor(private val repository: CompleteProfileRepository) :
    ViewModel() {

    private val _completeProfileUiState =
        MutableStateFlow<CompleteProfileUiState>(CompleteProfileUiState.Empty)

    val completeProfileUiState: StateFlow<CompleteProfileUiState> = _completeProfileUiState

    fun updateUser(id: String, user: User) {
        viewModelScope.launch {
            _completeProfileUiState.value = CompleteProfileUiState.Loading
            repository.updateUser(id, user).catch {
                _completeProfileUiState.value = CompleteProfileUiState.Error(it.message.toString())
                Log.e("error", it.message.toString())
            }.collect {
                _completeProfileUiState.value = CompleteProfileUiState.Success(it)
            }
        }
    }

}