package com.sparklead.evocharge.serviceImp

import android.util.Log
import com.sparklead.evocharge.models.SignUpRequest
import com.sparklead.evocharge.models.SignUpResponse
import com.sparklead.evocharge.remote.HttpRoutes
import com.sparklead.evocharge.service.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthServiceImp(private val client: HttpClient) : AuthService {

    override suspend fun signUpUser(signUpRequest: SignUpRequest): SignUpResponse {
        return try {
                client.post {
                    url(HttpRoutes.SIGNUP)
                    contentType(ContentType.Application.Json)
                    body = signUpRequest
                }

        } catch (e: Exception) {
            Log.e("error in SignUp", e.message.toString())
            throw e
        }
    }
}