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

sealed class HomeUiStatePetugas {
    data class Success(val petugas : List<Petugas>) : HomeUiStatePetugas()
    object Error : HomeUiStatePetugas()
    object Loading : HomeUiStatePetugas()
}

class HomeViewModelPetugas(private val ptg: PetugasRepository) : ViewModel(){
    var ptgUiState: HomeUiStatePetugas by mutableStateOf(HomeUiStatePetugas.Loading)
        private set

    init {
        getPtg()
    }

    fun getPtg(){
        viewModelScope.launch {
            ptgUiState = HomeUiStatePetugas.Loading
            ptgUiState = try {
                HomeUiStatePetugas.Success(ptg.getPetugas().data)
            } catch (e: IOException) {
                HomeUiStatePetugas.Error
            } catch (e: IOException) {
                HomeUiStatePetugas.Error
            }
        }
    }
}