package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.PetugasRepository
import com.example.uas.ui.view.petugas.DestinasiUpdatePetugas
import com.example.uas.ui.viewmodel.hewan.FormErrorState
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdateViewModelPetugas(
    savedStateHandle: SavedStateHandle,
    private val petugasRepository: PetugasRepository
) : ViewModel() {

    var updateUIStatePetugas by mutableStateOf(InsertUiStatePetugas())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    private val _idPetugas: String = checkNotNull(savedStateHandle[DestinasiUpdatePetugas.IDPETUGAS])

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            updateUIStatePetugas = updateUIStatePetugas.copy(isEntryValidPetugas = FormErrorStatePetugas())
        }
    }

    init {
        viewModelScope.launch {
            updateUIStatePetugas = petugasRepository.getPetugasById(_idPetugas)
                .data
                .toUiStatePtg()
        }
    }

    fun updateInsertPtgState(insertUiEventPetugas: InsertUiEventPetugas){
        updateUIStatePetugas = InsertUiStatePetugas(insertUiEventPetugas = insertUiEventPetugas)
    }

    suspend fun updateData(){
        if (validateFields()){
            viewModelScope.launch {
                try {
                    petugasRepository.updatePetugas(_idPetugas, updateUIStatePetugas.insertUiEventPetugas.toPtg())
                    updateUIStatePetugas = InsertUiStatePetugas(
                        snackBarMessage = "Data Petugas berhasil diubah",
                        insertUiEventPetugas = InsertUiEventPetugas(),
                        isEntryValidPetugas = FormErrorStatePetugas()
                    )
                }catch (e: Exception){
                    updateUIStatePetugas = updateUIStatePetugas.copy(
                        snackBarMessage = "Data Petugas gagal diubah"
                    )
                }
            }
        }
        else{
            updateUIStatePetugas = updateUIStatePetugas.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateUIStatePetugas = updateUIStatePetugas.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = updateUIStatePetugas.insertUiEventPetugas
        val errorStatePetugas = FormErrorStatePetugas(
            namaPetugasError = if (event.namaPetugas.isNotEmpty()) null else "Nama Petugas tidak boleh kosong",
            jabatanError = if (event.jabatan.isNotEmpty()) null else "Jabatan tidak boleh kosong"
        )
        updateUIStatePetugas = updateUIStatePetugas.copy(isEntryValidPetugas = errorStatePetugas)

        if (!errorStatePetugas.isValid()){
            hapusErrorState()
        }
        return errorStatePetugas.isValid()
    }
}