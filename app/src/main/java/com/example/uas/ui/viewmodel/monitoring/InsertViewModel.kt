package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Monitoring
import com.example.uas.repository.MonitoringRepository
import kotlinx.coroutines.launch

class InsertViewModelMonitoring(private val mnt: MonitoringRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiStateMonitoring())
        private set

    fun updateInsertMntState(insertUiEventMonitoring: InsertUiEventMonitoring){
        uiState = InsertUiStateMonitoring(insertUiEventMonitoring=insertUiEventMonitoring)
    }

    suspend fun insertMnt(){
        viewModelScope.launch {
            try {
                mnt.insertMonitoring(uiState.insertUiEventMonitoring.toMnt())
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiStateMonitoring(
    val insertUiEventMonitoring: InsertUiEventMonitoring = InsertUiEventMonitoring()
)

data class InsertUiEventMonitoring(
    val idMonitoring:Int=0,
    val idPetugas:Int=0,
    val idKandang:String="",
    val tanggalMonitoring:String="",
    val hewanSakit:Int=0,
    val hewanSehat:Int=0,
    val status:String="",
)

fun InsertUiEventMonitoring.toMnt(): Monitoring = Monitoring(
    idMonitoring = idMonitoring,
    idPetugas = idPetugas,
    idKandang = idKandang,
    tanggalMonitoring = tanggalMonitoring,
    hewanSakit = hewanSakit,
    hewanSehat = hewanSehat,
    status = status
)

fun Monitoring.toUiStateMnt(): InsertUiStateMonitoring = InsertUiStateMonitoring(
    insertUiEventMonitoring = toInsertUiEventMonitoring()
)

fun Monitoring.toInsertUiEventMonitoring(): InsertUiEventMonitoring = InsertUiEventMonitoring(
    idMonitoring = idMonitoring,
    idPetugas = idPetugas,
    idKandang = idKandang,
    tanggalMonitoring = tanggalMonitoring,
    hewanSakit = hewanSakit,
    hewanSehat = hewanSehat,
    status = status
)

