package com.example.uas.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.repository.HewanRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val hewan : List<Hewan>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

class HomeViewModel(private val hwn: HewanRepository) : ViewModel(){
    var hwnUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getHwn()
    }

    fun getHwn(){
        viewModelScope.launch {
            hwnUiState = HomeUiState.Loading
            hwnUiState = try {
                HomeUiState.Success(hwn.getHewan().data)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: IOException) {
                HomeUiState.Error
            }
        }
    }
}