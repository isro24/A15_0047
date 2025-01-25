package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.MonitoringRepository
import com.example.uas.ui.view.monitoring.DestinasiUpdateMonitoring
import kotlinx.coroutines.launch

class UpdateViewModelMonitoring(
    savedStateHandle: SavedStateHandle,
    private val monitoringRepository: MonitoringRepository
) : ViewModel() {

    var updateUIStateMonitoring by mutableStateOf(InsertUiStateMonitoring())
        private set

    private val _idMonitoring: String = checkNotNull(savedStateHandle[DestinasiUpdateMonitoring.IDMONITORING])

    init {
        viewModelScope.launch {
            updateUIStateMonitoring = monitoringRepository.getMonitoringById(_idMonitoring)
                .data
                .toUiStateMnt()
        }
    }

    fun updateInsertMntState(insertUiEventMonitoring: InsertUiEventMonitoring){
        updateUIStateMonitoring = InsertUiStateMonitoring(insertUiEventMonitoring = insertUiEventMonitoring)
    }

    suspend fun updateData(){
        viewModelScope.launch {
            try {
                monitoringRepository.updateMonitoring(_idMonitoring, updateUIStateMonitoring.insertUiEventMonitoring.toMnt())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}