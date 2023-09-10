package com.sparklead.evocharge.ui.repositories

import com.sparklead.evocharge.models.User
import com.sparklead.evocharge.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompleteProfileRepositoryImp @Inject constructor(private val service: UserService) : CompleteProfileRepository {

    override fun updateUser(id: String, user: User): Flow<String> {
        return flow {
          emit(service.updateUser(id,user))
        }
    }

}