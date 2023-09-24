package com.sparklead.evocharge.models

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(

    var user: User
)