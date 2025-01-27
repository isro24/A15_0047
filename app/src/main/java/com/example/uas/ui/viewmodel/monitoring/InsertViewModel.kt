package com.example.uas.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.model.Monitoring
import com.example.uas.model.Petugas
import com.example.uas.repository.MonitoringRepository
import com.example.uas.ui.viewmodel.hewan.FormErrorState
import com.example.uas.ui.viewmodel.hewan.InsertUiEvent
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertViewModelMonitoring(private val mnt: MonitoringRepository): ViewModel(){
    var uiStateMonitoring by mutableStateOf(InsertUiStateMonitoring())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    init {
        fetchPetugas()
        fetchKandang()
        fetchHewan()
    }
    var listPetugas by mutableStateOf<List<Petugas>>(emptyList())
        private set

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            uiStateMonitoring = uiStateMonitoring.copy(isEntryValidMonitoring = FormErrorStateMonitoring())
        }
    }
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
        uiStateMonitoring = InsertUiStateMonitoring(insertUiEventMonitoring=insertUiEventMonitoring.copy(status=statusBaru))
    }

    suspend fun insertMnt(){
        if (validateFields()){
            viewModelScope.launch {
                try {
                    mnt.insertMonitoring(uiStateMonitoring.insertUiEventMonitoring.toMnt())
                    uiStateMonitoring = InsertUiStateMonitoring(
                        snackBarMessage = "Data Monitoring berhasil ditambahkan",
                        insertUiEventMonitoring = InsertUiEventMonitoring(),
                        isEntryValidMonitoring = FormErrorStateMonitoring()
                    )
                }catch (e:Exception){
                    uiStateMonitoring = uiStateMonitoring.copy(
                        snackBarMessage = "Data Monitoring gagal disimpan"
                    )
                }
            }
        }
        else{
            uiStateMonitoring = uiStateMonitoring.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }

    fun resetSnackBarMessage() {
        uiStateMonitoring = uiStateMonitoring.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = uiStateMonitoring.insertUiEventMonitoring
        val errorStateMonitoring = FormErrorStateMonitoring(
            idPetugasError = if(event.idPetugas != null) null else "Nama Petugas tidak boleh kosong" ,
            idKandangError = if(event.idKandang.isNotEmpty()) null else "Data Kandang tidak boleh kosong",
            tanggalMonitoringError = if(event.tanggalMonitoring.isNotEmpty()) null else "Tanggal Monitoring tidak boleh kosong",
            hewanSakitError = if(event.hewanSakit != null) null else "Hewan Sakit tidak boleh kosong",
            hewanSehatError = if(event.hewanSehat != null) null else "Hewan Sehat tidak boleh kosong",
            statusError = if(event.status.isNotEmpty()) null else "Status tidak boleh kosong"

        )
        uiStateMonitoring = uiStateMonitoring.copy(isEntryValidMonitoring = errorStateMonitoring)

        if (!errorStateMonitoring.isValid()){
            hapusErrorState()
        }
        return errorStateMonitoring.isValid()
    }
}

data class InsertUiStateMonitoring(
    val insertUiEventMonitoring: InsertUiEventMonitoring = InsertUiEventMonitoring(),
    val isEntryValidMonitoring: FormErrorStateMonitoring = FormErrorStateMonitoring(),
    val snackBarMessage: String? = null
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

data class FormErrorStateMonitoring(
    val idPetugasError: String?=null,
    val idKandangError:String?=null,
    val tanggalMonitoringError:String?=null,
    val hewanSakitError:String?=null,
    val hewanSehatError:String?=null,
    val statusError:String?=null
){
    fun isValid(): Boolean{
        return idPetugasError == null
                && idKandangError == null
                && tanggalMonitoringError == null
                && hewanSakitError == null
                && hewanSehatError == null
                && statusError == null

    }
}

