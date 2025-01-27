package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.MonitoringRepository
import com.example.uas.ui.view.monitoring.DestinasiUpdateMonitoring
import com.example.uas.ui.viewmodel.hewan.FormErrorState
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdateViewModelMonitoring(
    savedStateHandle: SavedStateHandle,
    private val monitoringRepository: MonitoringRepository
) : ViewModel() {

    var updateUIStateMonitoring by mutableStateOf(InsertUiStateMonitoring())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    private val _idMonitoring: String = checkNotNull(savedStateHandle[DestinasiUpdateMonitoring.IDMONITORING])

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            updateUIStateMonitoring = updateUIStateMonitoring.copy(isEntryValidMonitoring = FormErrorStateMonitoring())
        }
    }

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
        if (validateFields()){
            viewModelScope.launch {
                try {
                    monitoringRepository.updateMonitoring(_idMonitoring, updateUIStateMonitoring.insertUiEventMonitoring.toMnt())
                    updateUIStateMonitoring = InsertUiStateMonitoring(
                        snackBarMessage = "Data Monitoring berhasil diubah",
                        insertUiEventMonitoring = InsertUiEventMonitoring(),
                        isEntryValidMonitoring = FormErrorStateMonitoring()
                    )
                }catch (e: Exception){
                    updateUIStateMonitoring = updateUIStateMonitoring.copy(
                        snackBarMessage = "Data Monitoring gagal diubah"
                    )
                }
            }
        }
        else{
            updateUIStateMonitoring = updateUIStateMonitoring.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateUIStateMonitoring = updateUIStateMonitoring.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = updateUIStateMonitoring.insertUiEventMonitoring
        val errorStateMonitoring = FormErrorStateMonitoring(
            idPetugasError = if(event.idPetugas != null) null else "Nama Petugas tidak boleh kosong" ,
            idKandangError = if(event.idKandang.isNotEmpty()) null else "Data Kandang tidak boleh kosong",
            tanggalMonitoringError = if(event.tanggalMonitoring.isNotEmpty()) null else "Tanggal Monitoring tidak boleh kosong",
            hewanSakitError = if(event.hewanSakit != null) null else "Hewan Sakit tidak boleh kosong",
            hewanSehatError = if(event.hewanSehat != null) null else "Hewan Sehat tidak boleh kosong",
            statusError = if(event.status.isNotEmpty()) null else "Status tidak boleh kosong"

        )
        updateUIStateMonitoring = updateUIStateMonitoring.copy(isEntryValidMonitoring = errorStateMonitoring)

        if (!errorStateMonitoring.isValid()){
            hapusErrorState()
        }
        return errorStateMonitoring.isValid()
    }
}