package com.sparklead.evocharge.service

import com.sparklead.evocharge.models.SignInRequest
import com.sparklead.evocharge.models.SignInResponse
import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.models.SignUpResponse

interface AuthService {

    suspend fun signUpUser(signUpRequest: SignUpRequest): SignUpResponse

    suspend fun signInUser(signInRequest: SignInRequest): SignInResponse
}