package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.SignUpResponse
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {

    fun signUser(firstName : String,lastName:String,email : String,password : String) : Flow<SignUpResponse>
}