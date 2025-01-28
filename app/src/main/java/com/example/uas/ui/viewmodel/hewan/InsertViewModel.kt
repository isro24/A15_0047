package com.example.uas.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.repository.HewanRepository
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertViewModel(private val hwn: HewanRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiState())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }
    fun updateInsertHwnState(insertUiEvent: InsertUiEvent){
        uiState = InsertUiState(insertUiEvent=insertUiEvent)
    }

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            uiState = uiState.copy(isEntryValid = FormErrorState())
        }
    }

    suspend fun insertHwn(){
        if(validateFields()){
            viewModelScope.launch {
                try {
                    hwn.insertHewan(uiState.insertUiEvent.toHwn())
                    uiState = InsertUiState(
                        snackBarMessage = "Data Hewan berhasil ditambahkan",
                        insertUiEvent = InsertUiEvent(),
                        isEntryValid = FormErrorState()
                    )
                }catch (e:Exception){
                    uiState = uiState.copy(
                        snackBarMessage = "Data Hewan gagal disimpan"
                    )
                }
            }
        }
        else{
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }

    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = uiState.insertUiEvent
        val errorState = FormErrorState(
            namaHewanError = if(event.namaHewan.isNotEmpty()) null else "Nama Hewan tidak boleh kosong" ,
            tipePakanError = if(event.tipePakan.isNotEmpty()) null else "Tipe Pakan tidak boleh kosong",
            populasiError = if(event.populasi != null) null else "Populasi tidak boleh kosong",
            zonaWilayahError = if(event.zonaWilayah.isNotEmpty()) null else "Zona Wilayah tidak boleh kosong"
        )
        uiState = uiState.copy(isEntryValid = errorState)

        if (!errorState.isValid()){
            hapusErrorState()
        }
        return errorState.isValid()
    }
}

data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null

)

data class InsertUiEvent(
    val idHewan:Int=0,
    val namaHewan:String="",
    val tipePakan:String="",
    val populasi:Int?=null,
    val zonaWilayah:String="",
)

fun InsertUiEvent.toHwn(): Hewan = Hewan(
    idHewan = idHewan,
    namaHewan=namaHewan,
    tipePakan=tipePakan,
    populasi=populasi?:0,
    zonaWilayah=zonaWilayah,
)

fun Hewan.toUiStateHwn(): InsertUiState = InsertUiState(
    insertUiEvent = toInsertUiEvent()
)

fun Hewan.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    idHewan=idHewan,
    namaHewan=namaHewan,
    tipePakan=tipePakan,
    populasi=populasi,
    zonaWilayah=zonaWilayah
)

data class FormErrorState(
    val namaHewanError:String?=null,
    val tipePakanError:String?=null,
    val populasiError:String?=null,
    val zonaWilayahError:String?=null
){
    fun isValid(): Boolean{
        return namaHewanError == null
                && tipePakanError == null
                && populasiError == null
                && zonaWilayahError == null

    }
}

