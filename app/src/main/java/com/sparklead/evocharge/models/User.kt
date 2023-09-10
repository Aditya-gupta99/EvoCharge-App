package com.sparklead.evocharge.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class User(

    var userId: String? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var email: String? = null,

    var brand: String? = null,

    var model: String? = null
)
