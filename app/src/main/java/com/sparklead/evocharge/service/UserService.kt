package com.sparklead.evocharge.service

import com.sparklead.evocharge.models.User

interface UserService {

    suspend fun updateUser(id: String, user: User): String

}