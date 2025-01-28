package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Monitoring
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