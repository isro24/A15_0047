package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Petugas
import com.example.uas.repository.PetugasRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val petugas : List<Petugas>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModel(private val ptg: PetugasRepository) : ViewModel(){
    var ptgUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getPtg()
    }

    fun getPtg(){
        viewModelScope.launch {
            ptgUiState = HomeUiState.Loading
            ptgUiState = try {
                HomeUiState.Success(ptg.getPetugas().data)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: IOException) {
                HomeUiState.Error
            }
        }
    }
}