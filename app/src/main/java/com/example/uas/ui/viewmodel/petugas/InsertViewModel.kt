package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Petugas
import com.example.uas.repository.PetugasRepository
import com.example.uas.ui.viewmodel.hewan.FormErrorState
import com.example.uas.ui.viewmodel.hewan.InsertUiEvent
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertViewModelPetugas(private val ptg: PetugasRepository): ViewModel(){
    var uiStatePetugas by mutableStateOf(InsertUiStatePetugas())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            uiStatePetugas = uiStatePetugas.copy(isEntryValidPetugas = FormErrorStatePetugas())
        }
    }

    fun updateInsertPtgState(insertUiEventPetugas: InsertUiEventPetugas){
        uiStatePetugas = InsertUiStatePetugas(insertUiEventPetugas=insertUiEventPetugas)
    }

    suspend fun insertPtg(){
        if (validateFields()){
            viewModelScope.launch {
                try {
                    ptg.insertPetugas(uiStatePetugas.insertUiEventPetugas.toPtg())
                    uiStatePetugas = InsertUiStatePetugas(
                        snackBarMessage = "Data Hewan berhasil ditambahkan",
                        insertUiEventPetugas = InsertUiEventPetugas(),
                        isEntryValidPetugas = FormErrorStatePetugas()
                    )
                }catch (e:Exception){
                    uiStatePetugas = uiStatePetugas.copy(
                        snackBarMessage = "Data Petugas gagal disimpan"
                    )
                }
            }
        }
        else{
            uiStatePetugas = uiStatePetugas.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }

    fun resetSnackBarMessage() {
        uiStatePetugas = uiStatePetugas.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = uiStatePetugas.insertUiEventPetugas
        val errorStatePetugas = FormErrorStatePetugas(
            namaPetugasError = if (event.namaPetugas.isNotEmpty()) null else "Nama Petugas tidak boleh kosong",
            jabatanError = if (event.jabatan.isNotEmpty()) null else "Jabatan tidak boleh kosong"
        )
        uiStatePetugas = uiStatePetugas.copy(isEntryValidPetugas = errorStatePetugas)

        if (!errorStatePetugas.isValid()){
            hapusErrorState()
        }
        return errorStatePetugas.isValid()
    }
}

data class InsertUiStatePetugas(
    val insertUiEventPetugas: InsertUiEventPetugas = InsertUiEventPetugas(),
    val isEntryValidPetugas: FormErrorStatePetugas = FormErrorStatePetugas(),
    val snackBarMessage: String? = null
)

data class InsertUiEventPetugas(
    val idPetugas:Int=0,
    val namaPetugas:String="",
    val jabatan:String="",
)

fun InsertUiEventPetugas.toPtg(): Petugas = Petugas(
    idPetugas=idPetugas,
    namaPetugas=namaPetugas,
    jabatan=jabatan
)

fun Petugas.toUiStatePtg(): InsertUiStatePetugas = InsertUiStatePetugas(
    insertUiEventPetugas = toInsertUiEventPetugas()
)

fun Petugas.toInsertUiEventPetugas(): InsertUiEventPetugas = InsertUiEventPetugas(
    idPetugas=idPetugas,
    namaPetugas=namaPetugas,
    jabatan=jabatan
)

data class FormErrorStatePetugas(
    val namaPetugasError: String?=null,
    val jabatanError: String?=null
){
    fun isValid(): Boolean{
        return namaPetugasError == null
                && jabatanError == null
    }
}