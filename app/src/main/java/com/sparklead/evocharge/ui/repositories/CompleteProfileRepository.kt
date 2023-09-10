package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.User
import kotlinx.coroutines.flow.Flow

interface CompleteProfileRepository {

    fun updateUser(id : String,user: User) : Flow<String>

}