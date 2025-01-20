package com.example.uas.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.repository.HewanRepository
import com.example.uas.ui.view.hewan.DestinasiDetailHewan
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val hewanRepository: HewanRepository
) : ViewModel() {
    private val idHewan: String = checkNotNull(savedStateHandle[DestinasiDetailHewan.IDHEWAN])

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState())
        private set

    init {
        getHewanByNim()
    }

    private fun getHewanByNim() {
        viewModelScope.launch {
            detailUiState = DetailUiState(isLoading = true)
            try {
                val result = hewanRepository.getHewanById(idHewan).data
                detailUiState = DetailUiState(
                    detailUiEvent = result.toDetailUiEvent(),
                    isLoading = false
                )
            } catch (e: Exception) {
                detailUiState = DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown"
                )
            }
        }
    }

    fun deleteHwn() {
        viewModelScope.launch {
            detailUiState = DetailUiState(isLoading = true)
            try {
                hewanRepository.deleteHewan(idHewan)

                detailUiState = DetailUiState(isLoading = false)
            } catch (e: Exception) {
                detailUiState = DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown Error"
                )
            }
        }
    }
}


data class DetailUiState(
    val detailUiEvent: InsertUiEvent = InsertUiEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == InsertUiEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != InsertUiEvent()
}

fun Hewan.toDetailUiEvent(): InsertUiEvent{
    return InsertUiEvent(
        namaHewan = namaHewan,
        tipePakan = tipePakan,
        populasi = populasi,
        zonaWilayah = zonaWilayah
    )
}