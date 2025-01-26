package com.example.uas.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.model.Monitoring
import com.example.uas.model.Petugas
import com.example.uas.repository.MonitoringRepository
import kotlinx.coroutines.launch

class InsertViewModelMonitoring(private val mnt: MonitoringRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiStateMonitoring())
        private set

    init {
        fetchPetugas()
        fetchKandang()
        fetchHewan()
    }
    var listPetugas by mutableStateOf<List<Petugas>>(emptyList())
        private set

    private fun fetchPetugas(){
        viewModelScope.launch {
            try {
                val response = mnt.getPetugas()
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
                val response = mnt.getKandang()
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
                val response = mnt.getHewan()
                if (response.status){
                    listHewan = response.data
                    Log.d("FetchHewan", "Data Hewan: ${listHewan}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun statusHewan(hewanSakit: Int?, hewanSehat: Int?): String{
        val total = (hewanSakit?: 0) + (hewanSehat?: 0)
        if(total == 0) return  "Tidak Diketahui"
        val presentase = ((hewanSakit?:0).toDouble() / total) * 100
        return when{
            presentase < 10 -> "Aman"
            presentase < 50 -> "Waspada"
            else -> "Kritis"
        }
    }

    fun updateInsertMntState(insertUiEventMonitoring: InsertUiEventMonitoring){
        val statusBaru = statusHewan(
            hewanSakit = insertUiEventMonitoring.hewanSakit,
            hewanSehat = insertUiEventMonitoring.hewanSehat
        )
        uiState = InsertUiStateMonitoring(insertUiEventMonitoring=insertUiEventMonitoring.copy(status=statusBaru))
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
    val idMonitoring: Int? = null,
    val idPetugas: Int? = null,
    val idKandang: String = "",
    val tanggalMonitoring: String = "",
    val hewanSakit: Int? = null,
    val hewanSehat: Int? = null,
    val status: String = "",
)

fun InsertUiEventMonitoring.toMnt(): Monitoring = Monitoring(
    idMonitoring = idMonitoring ?: 0,
    idPetugas = idPetugas?: 0,
    idKandang = idKandang,
    tanggalMonitoring = tanggalMonitoring,
    hewanSakit = hewanSakit?: 0,
    hewanSehat = hewanSehat?: 0,
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

