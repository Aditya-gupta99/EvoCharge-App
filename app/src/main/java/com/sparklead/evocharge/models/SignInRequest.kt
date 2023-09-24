package com.sparklead.evocharge.models

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(

    var email: String,

    var password: String
)
