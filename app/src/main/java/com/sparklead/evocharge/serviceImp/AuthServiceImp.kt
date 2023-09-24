package com.sparklead.evocharge.serviceImp

import android.util.Log
import com.sparklead.evocharge.models.SignInRequest
import com.sparklead.evocharge.models.SignInResponse
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

    override suspend fun signInUser(signInRequest: SignInRequest): SignInResponse {
        return try {
            client.post {
                url(HttpRoutes.SIGNIN)
                contentType(ContentType.Application.Json)
                body = signInRequest
            }

        } catch (e: Exception) {
            Log.e("error in SignIn", e.message.toString())
            throw e
        }
    }
}