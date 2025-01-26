package com.example.uas.repository

import com.example.uas.model.AllHewanResponse
import com.example.uas.model.AllKandangResponse
import com.example.uas.model.AllMonitoringResponse
import com.example.uas.model.AllPetugasResponse
import com.example.uas.model.Monitoring
import com.example.uas.model.MonitoringDetailResponse
import com.example.uas.model.PetugasDetailResponse
import com.example.uas.service_api.MonitoringService
import com.example.uas.service_api.PetugasService
import java.io.IOException

interface MonitoringRepository{
    suspend fun insertMonitoring(monitoring: Monitoring)

    suspend fun getMonitoring(): AllMonitoringResponse

    suspend fun getPetugas(): AllPetugasResponse

    suspend fun getKandang(): AllKandangResponse

    suspend fun getHewan(): AllHewanResponse

    suspend fun updateMonitoring(idMonitoring: String, monitoring: Monitoring)

    suspend fun deleteMonitoring(idMonitoring: String)

    suspend fun getMonitoringById(idMonitoring: String): MonitoringDetailResponse
}

class NetworkMonitoringRepository(
    private val monitoringApiService: MonitoringService,

): MonitoringRepository{
    override suspend fun insertMonitoring(monitoring: Monitoring) {
        monitoringApiService.insertMonitoring(monitoring)
    }

    override suspend fun updateMonitoring(idMonitoring: String, monitoring: Monitoring) {
        monitoringApiService.updateMonitoring(idMonitoring, monitoring)
    }

    override suspend fun deleteMonitoring(idMonitoring: String) {
        try {
            val response = monitoringApiService.deleteMonitoring(idMonitoring)
            if (!response.isSuccessful){
                throw IOException("Failed to delete Monitoring. HTTP Status code: " +
                        "${response.code()}")
            }else{
                response.message()
                println(response.message())
            }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getMonitoring(): AllMonitoringResponse =
        monitoringApiService.getAllMonitoring()

    override suspend fun getPetugas(): AllPetugasResponse =
        monitoringApiService.getAllPetugas()

    override suspend fun getKandang(): AllKandangResponse =
        monitoringApiService.getAllKandang()

    override suspend fun getHewan(): AllHewanResponse =
        monitoringApiService.getAllHewan()

    override suspend fun getMonitoringById(idMonitoring: String): MonitoringDetailResponse {
        return monitoringApiService.getMonitoringById(idMonitoring)
    }
}