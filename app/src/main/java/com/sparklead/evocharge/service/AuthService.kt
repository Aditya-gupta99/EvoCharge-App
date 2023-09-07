package com.sparklead.evocharge.service

import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.models.SignUpResponse
import com.sparklead.evocharge.serviceImp.AuthServiceImp
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.json.Json

interface AuthService {

    suspend fun signUpUser(signUpRequest: SignUpRequest) : SignUpResponse

    companion object {

        private val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
        fun create() : AuthService {
            return AuthServiceImp(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature){
                        serializer = KotlinxSerializer(json)
                    }
                }
            )
        }
    }

}