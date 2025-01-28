package com.example.uas.ui.viewmodel.kandang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.KandangRepository
import com.example.uas.ui.view.kandang.DestinasiUpdateKandang
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdateViewModelKandang(
    savedStateHandle: SavedStateHandle,
    private val kandangRepository: KandangRepository
) : ViewModel() {

    var updateUIStateKandang by mutableStateOf(InsertUiStateKandang())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    private val _idKandang: String = checkNotNull(savedStateHandle[DestinasiUpdateKandang.IDKANDANG])

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            updateUIStateKandang = updateUIStateKandang.copy(isEntryValidKandang = FormErrorStateKandang())
        }
    }

    init {
        viewModelScope.launch {
            updateUIStateKandang = kandangRepository.getKandangById(_idKandang)
                .data
                .toUiStateKnd()
        }
    }


    fun updateInsertKndState(insertUiEventKandang: InsertUiEventKandang){
        updateUIStateKandang = InsertUiStateKandang(insertUiEventKandang = insertUiEventKandang)
    }

    suspend fun updateData(){
       if (validateFields()){
           viewModelScope.launch {
               try {
                   kandangRepository.updateKandang(_idKandang, updateUIStateKandang.insertUiEventKandang.toKnd())
                   updateUIStateKandang = InsertUiStateKandang(
                       snackBarMessage = "Data Kandang berhasil diubah",
                       insertUiEventKandang = InsertUiEventKandang(),
                       isEntryValidKandang = FormErrorStateKandang()
                   )
               }catch (e: Exception){
                   updateUIStateKandang= updateUIStateKandang.copy(
                       snackBarMessage = "Data Kandang gagal diubah"
                   )
               }
           }
       }
        else{
            updateUIStateKandang = updateUIStateKandang.copy(
                snackBarMessage = "Data kandang Gagal diubah"
            )
       }
    }

    fun resetSnackBarMessage() {
        updateUIStateKandang = updateUIStateKandang.copy(snackBarMessage = null)
    }
    fun validateFields(): Boolean{
        val event = updateUIStateKandang.insertUiEventKandang
        val errorState = FormErrorStateKandang(
            idKandangError = if (event.idKandang.isNotEmpty()) null else "Id Kandang tidak boleh kosong",
            idHewanError = if (event.idHewan != null) null else "Id Hewan tidak boleh kosong",
            kapasitasError = if (event.kapasitas != null) null else "Kapasitas tidak boleh kosong",
            lokasiError = if (event.lokasi.isNotEmpty()) null else "Lokasi tidak boleh kosong",
        )
        updateUIStateKandang = updateUIStateKandang.copy(isEntryValidKandang = errorState)

        if (!errorState.isValid()){
            hapusErrorState()
        }
        return errorState.isValid()
    }
}