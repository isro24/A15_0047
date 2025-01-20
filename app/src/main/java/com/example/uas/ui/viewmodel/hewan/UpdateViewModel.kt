package com.example.uas.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.HewanRepository
import com.example.uas.ui.view.hewan.DestinasiUpdateHewan
import kotlinx.coroutines.launch

class UpdateViewModel(
    savedStateHandle: SavedStateHandle,
    private val hewanRepository: HewanRepository
) : ViewModel() {

    var updateUIState by mutableStateOf(InsertUiState())
        private set

    private val _idHewan: String = checkNotNull(savedStateHandle[DestinasiUpdateHewan.IDHEWAN])

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
        viewModelScope.launch {
            try {
                hewanRepository.updateHewan(_idHewan, updateUIState.insertUiEvent.toHwn())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}