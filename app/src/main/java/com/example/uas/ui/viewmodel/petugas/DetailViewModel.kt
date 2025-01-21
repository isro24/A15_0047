package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Petugas
import com.example.uas.repository.PetugasRepository
import com.example.uas.ui.view.petugas.DestinasiDetailPetugas
import com.example.uas.ui.viewmodel.petugas.InsertUiEventPetugas
import kotlinx.coroutines.launch

class DetailViewModelPetugas(
    savedStateHandle: SavedStateHandle,
    private val petugasRepository: PetugasRepository
) : ViewModel() {
    private val idPetugas: String = checkNotNull(savedStateHandle[DestinasiDetailPetugas.IDPETUGAS])

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState())
        private set

    init {
        getHewanById()
    }

    fun getHewanById() {
        viewModelScope.launch {
            detailUiState = DetailUiState(isLoading = true)
            try {
                val result = petugasRepository.getPetugasById(idPetugas).data
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

    fun deletePtg() {
        viewModelScope.launch {
            detailUiState = DetailUiState(isLoading = true)
            try {
                petugasRepository.deletePetugas(idPetugas)

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
    val detailUiEvent: InsertUiEventPetugas = InsertUiEventPetugas(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == InsertUiEventPetugas()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != InsertUiEventPetugas()
}

fun Petugas.toDetailUiEvent(): InsertUiEventPetugas{
    return InsertUiEventPetugas(
        namaPetugas = namaPetugas,
        jabatan = jabatan
    )
}