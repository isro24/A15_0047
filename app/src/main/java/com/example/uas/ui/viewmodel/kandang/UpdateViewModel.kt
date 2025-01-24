package com.example.uas.ui.viewmodel.kandang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.repository.KandangRepository
import com.example.uas.ui.view.kandang.DestinasiUpdateKandang
import kotlinx.coroutines.launch

class UpdateViewModelKandang(
    savedStateHandle: SavedStateHandle,
    private val kandangRepository: KandangRepository
) : ViewModel() {

    var updateUIStateKandang by mutableStateOf(InsertUiStateKandang())
        private set

    private val _idKandang: String = checkNotNull(savedStateHandle[DestinasiUpdateKandang.IDKANDANG])

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
        viewModelScope.launch {
            try {
                kandangRepository.updateKandang(_idKandang, updateUIStateKandang.insertUiEventKandang.toKnd())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}