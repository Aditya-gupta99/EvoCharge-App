package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.models.SignUpResponse
import com.sparklead.evocharge.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepositoryImp @Inject constructor(private val service: AuthService) : SignUpRepository {

    override fun signUser(firstName: String, lastName: String, email: String, password: String): Flow<SignUpResponse> {
        return flow {
            emit(service.signUpUser(SignUpRequest(firstName,lastName, email, password)))
        }
    }
}