package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.SignInResponse
import kotlinx.coroutines.flow.Flow

interface SignInRepository {

    fun signInUser(email : String,password : String) : Flow<SignInResponse>

}