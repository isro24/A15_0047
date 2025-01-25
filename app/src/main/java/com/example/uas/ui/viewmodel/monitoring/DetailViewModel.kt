package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Monitoring
import com.example.uas.repository.MonitoringRepository
import com.example.uas.ui.view.monitoring.DestinasiDetailMonitoring
import kotlinx.coroutines.launch

class DetailViewModelMonitoring(
    savedStateHandle: SavedStateHandle,
    private val monitoringRepository: MonitoringRepository
) : ViewModel() {
    private val idMonitoring: String = checkNotNull(savedStateHandle[DestinasiDetailMonitoring.IDMONITORING])

    var detailUiStateMonitoring: DetailUiStateMonitoring by mutableStateOf(DetailUiStateMonitoring())
        private set

    init {
        getMonitoringById()
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