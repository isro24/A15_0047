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
import kotlinx.coroutines.launch

class DetailViewModelPetugas(
    savedStateHandle: SavedStateHandle,
    private val petugasRepository: PetugasRepository
) : ViewModel() {
    private val idPetugas: String = checkNotNull(savedStateHandle[DestinasiDetailPetugas.IDPETUGAS])

    var detailUiStatePetugas: DetailUiStatePetugas by mutableStateOf(DetailUiStatePetugas())
        private set

    init {
        getPetugasById()
    }

    fun getPetugasById() {
        viewModelScope.launch {
            detailUiStatePetugas = DetailUiStatePetugas(isLoading = true)
            try {
                val result = petugasRepository.getPetugasById(idPetugas).data
                detailUiStatePetugas = DetailUiStatePetugas(
                    detailUiEventPetugas = result.toDetailUiEventPetugas(),
                    isLoading = false
                )
            } catch (e: Exception) {
                detailUiStatePetugas = DetailUiStatePetugas(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown"
                )
            }
        }
    }

    fun deletePtg() {
        viewModelScope.launch {
            detailUiStatePetugas = DetailUiStatePetugas(isLoading = true)
            try {
                petugasRepository.deletePetugas(idPetugas)

                detailUiStatePetugas = DetailUiStatePetugas(isLoading = false)
            } catch (e: Exception) {
                detailUiStatePetugas = DetailUiStatePetugas(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown Error"
                )
            }
        }
    }
}


data class DetailUiStatePetugas(
    val detailUiEventPetugas: InsertUiEventPetugas = InsertUiEventPetugas(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEventPetugas == InsertUiEventPetugas()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEventPetugas != InsertUiEventPetugas()
}

fun Petugas.toDetailUiEventPetugas(): InsertUiEventPetugas{
    return InsertUiEventPetugas(
        namaPetugas = namaPetugas,
        jabatan = jabatan
    )
}