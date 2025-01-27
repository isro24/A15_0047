package com.example.uas.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.model.Monitoring
import com.example.uas.model.Petugas
import com.example.uas.repository.MonitoringRepository
import com.example.uas.ui.view.monitoring.DestinasiDetailMonitoring
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.launch

class DetailViewModelMonitoring(
    savedStateHandle: SavedStateHandle,
    private val monitoringRepository: MonitoringRepository
) : ViewModel() {
    private val idMonitoring: String = checkNotNull(savedStateHandle[DestinasiDetailMonitoring.IDMONITORING])

    var detailUiStateMonitoring: DetailUiStateMonitoring by mutableStateOf(DetailUiStateMonitoring())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    init {
        getMonitoringById()
        fetchPetugas()
        fetchKandang()
        fetchHewan()
    }

    var listPetugas by mutableStateOf<List<Petugas>>(emptyList())
        private set

    private fun fetchPetugas(){
        viewModelScope.launch {
            try {
                val response = monitoringRepository.getPetugas()
                if (response.status){
                    listPetugas = response.data
                    Log.d("FetchPetugas", "Data Petugas: ${listPetugas}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    var listKandang by mutableStateOf<List<Kandang>>(emptyList())
        private set

    private fun fetchKandang(){
        viewModelScope.launch {
            try {
                val response = monitoringRepository.getKandang()
                if (response.status){
                    listKandang = response.data
                    Log.d("FetchKandang", "Data Kandang: ${listKandang}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    var listHewan by mutableStateOf<List<Hewan>>(emptyList())
        private set

    private fun fetchHewan(){
        viewModelScope.launch {
            try {
                val response = monitoringRepository.getHewan()
                if (response.status){
                    listHewan = response.data
                    Log.d("FetchHewan", "Data Hewan: ${listHewan}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMonitoringById() {
        viewModelScope.launch {
            detailUiStateMonitoring = DetailUiStateMonitoring(isLoading = true)
            try {
                val result = monitoringRepository.getMonitoringById(idMonitoring).data
                detailUiStateMonitoring = DetailUiStateMonitoring(
                    detailUiEventMonitoring = result.toDetailUiEventMonitoring(),
                    isLoading = false
                )
            } catch (e: Exception) {
                detailUiStateMonitoring = DetailUiStateMonitoring(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown"
                )
            }
        }
    }
    fun deleteMnt() {
        viewModelScope.launch {
            detailUiStateMonitoring = DetailUiStateMonitoring(isLoading = true)
            try {
                monitoringRepository.deleteMonitoring(idMonitoring)

                detailUiStateMonitoring = DetailUiStateMonitoring(isLoading = false)
            } catch (e: Exception) {
                detailUiStateMonitoring = DetailUiStateMonitoring(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown Error"
                )
            }
        }
    }
}


data class DetailUiStateMonitoring(
    val detailUiEventMonitoring: InsertUiEventMonitoring = InsertUiEventMonitoring(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEventMonitoring == InsertUiEventMonitoring()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEventMonitoring != InsertUiEventMonitoring()
}

fun Monitoring.toDetailUiEventMonitoring(): InsertUiEventMonitoring {
    return InsertUiEventMonitoring(
        idMonitoring = idMonitoring,
        idPetugas = idPetugas,
        idKandang = idKandang,
        tanggalMonitoring = tanggalMonitoring,
        hewanSakit = hewanSakit,
        hewanSehat = hewanSehat,
        status = status
    )
}