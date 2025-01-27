package com.example.uas.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.HewanRepository
import com.example.uas.ui.view.hewan.DestinasiUpdateHewan
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdateViewModel(
    savedStateHandle: SavedStateHandle,
    private val hewanRepository: HewanRepository
) : ViewModel() {

    var updateUIState by mutableStateOf(InsertUiState())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    private val _idHewan: String = checkNotNull(savedStateHandle[DestinasiUpdateHewan.IDHEWAN])

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            updateUIState = updateUIState.copy(isEntryValid = FormErrorState())
        }
    }

    init {
        viewModelScope.launch {
            updateUIState = hewanRepository.getHewanById(_idHewan)
                .data
                .toUiStateHwn()
        }
    }

    fun updateInsertHwnState(insertUiEvent: InsertUiEvent){
        updateUIState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun updateData(){
        if (validateFields()){
            viewModelScope.launch {
                try {
                    hewanRepository.updateHewan(_idHewan, updateUIState.insertUiEvent.toHwn())
                    updateUIState = InsertUiState(
                        snackBarMessage = "Data Hewan berhasil diubah",
                        insertUiEvent = InsertUiEvent(),
                        isEntryValid = FormErrorState()
                    )
                }catch (e: Exception){
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data Hewan gagal diubah"
                    )
                }
            }
        }
        else{
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data Hewan gagal diubah"
            )
        }
    }
    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = updateUIState.insertUiEvent
        val errorState = FormErrorState(
            namaHewanError = if(event.namaHewan.isNotEmpty()) null else "Nama Hewan tidak boleh kosong" ,
            tipePakanError = if(event.tipePakan.isNotEmpty()) null else "Tipe Pakan tidak boleh kosong",
            populasiError = if(event.populasi != null) null else "Populasi tidak boleh kosong",
            zonaWilayahError = if(event.zonaWilayah.isNotEmpty()) null else "Zona Wilayah tidak boleh kosong"
        )
        updateUIState = updateUIState.copy(isEntryValid = errorState)

        if (!errorState.isValid()){
            hapusErrorState()
        }
        return errorState.isValid()
    }
}