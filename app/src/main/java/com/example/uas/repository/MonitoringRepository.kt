package com.example.uas.repository

import com.example.uas.model.AllMonitoringResponse
import com.example.uas.model.Monitoring
import com.example.uas.model.MonitoringDetailResponse
import com.example.uas.service_api.MonitoringService

interface MonitoringRepository{
    suspend fun insertMonitoring(monitoring: Monitoring)

    suspend fun getMonitoring(): AllMonitoringResponse

    suspend fun updateMonitoring(idMonitoring: String, monitoring: Monitoring)

    suspend fun getMonitoringById(idMonitoring: String): MonitoringDetailResponse
}

class NetworkMonitoringRepository(
    private val monitoringApiService: MonitoringService
): MonitoringRepository{
    override suspend fun insertMonitoring(monitoring: Monitoring) {
        monitoringApiService.insertMonitoring(monitoring)
    }

    override suspend fun updateMonitoring(idMonitoring: String, monitoring: Monitoring) {
        monitoringApiService.updateMonitoring(idMonitoring, monitoring)
    }

    override suspend fun getMonitoring(): AllMonitoringResponse =
        monitoringApiService.getAllMonitoring()

    override suspend fun getMonitoringById(idMonitoring: String): MonitoringDetailResponse {
        return monitoringApiService.getMonitoringById(idMonitoring)
    }
}