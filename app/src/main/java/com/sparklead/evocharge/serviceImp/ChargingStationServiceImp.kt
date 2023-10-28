package com.sparklead.evocharge.serviceImp

import com.sparklead.evocharge.models.ChargingStationResponse
import com.sparklead.evocharge.remote.HttpRoutes
import com.sparklead.evocharge.service.ChargingStationService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ChargingStationServiceImp(private val client: HttpClient) : ChargingStationService {
    override suspend fun getChargingStationList(): ArrayList<ChargingStationResponse> {
        return try {
            client.get {
                url(HttpRoutes.STATION_LIST)
                contentType(ContentType.Application.Json)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}