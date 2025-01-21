package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.PetugasRepository
import com.example.uas.ui.view.petugas.DestinasiUpdatePetugas
import kotlinx.coroutines.launch

class UpdateViewModelPetugas(
    savedStateHandle: SavedStateHandle,
    private val petugasRepository: PetugasRepository
) : ViewModel() {

    var updateUIStatePetugas by mutableStateOf(InsertUiStatePetugas())
        private set

    private val _idPetugas: String = checkNotNull(savedStateHandle[DestinasiUpdatePetugas.IDPETUGAS])

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
        viewModelScope.launch {
            try {
                petugasRepository.updatePetugas(_idPetugas, updateUIStatePetugas.insertUiEventPetugas.toPtg())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}