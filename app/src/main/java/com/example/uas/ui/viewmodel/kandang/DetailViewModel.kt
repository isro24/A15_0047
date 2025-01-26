package com.example.uas.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.repository.KandangRepository
import com.example.uas.ui.view.kandang.DestinasiDetailKandang
import kotlinx.coroutines.launch

class DetailViewModelKandang(
    savedStateHandle: SavedStateHandle,
    private val kandangRepository: KandangRepository
) : ViewModel() {
    private val idKandang: String = checkNotNull(savedStateHandle[DestinasiDetailKandang.IDKANDANG])

    var detailUiStateKandang: DetailUiStateKandang by mutableStateOf(DetailUiStateKandang())
        private set

    var listHewan by mutableStateOf<List<Hewan>>(emptyList())
        private set


    init {
        getKandangById()
        fetchHewan()
    }

    private fun fetchHewan() {
        viewModelScope.launch {
            try {
                val response = kandangRepository.getHewan()
                if (response.status) {
                    listHewan = response.data
                    Log.d("FetchHewan", "Data Hewan: $listHewan")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getKandangById() {
        viewModelScope.launch {
            detailUiStateKandang = DetailUiStateKandang(isLoading = true)
            try {
                val result = kandangRepository.getKandangById(idKandang).data
                detailUiStateKandang = DetailUiStateKandang(
                    detailUiEventKandang = result.toDetailUiEventKandang(),
                    isLoading = false
                )
            } catch (e: Exception) {
                detailUiStateKandang = DetailUiStateKandang(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown"
                )
            }
        }
    }

    fun deleteKnd() {
        viewModelScope.launch {
            detailUiStateKandang = DetailUiStateKandang(isLoading = true)
            try {
                kandangRepository.deleteKandang(idKandang)

                detailUiStateKandang = DetailUiStateKandang(isLoading = false)
            } catch (e: Exception) {
                detailUiStateKandang = DetailUiStateKandang(
                    isLoading = false,
                    isError = true,
                    errorMessage = e.message ?: "Unknown Error"
                )
            }
        }
    }
}


data class DetailUiStateKandang(
    val detailUiEventKandang: InsertUiEventKandang = InsertUiEventKandang(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEventKandang == InsertUiEventKandang()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEventKandang != InsertUiEventKandang()
}

fun Kandang.toDetailUiEventKandang(): InsertUiEventKandang {
    return InsertUiEventKandang(
        idKandang = idKandang,
        idHewan =  idHewan,
        kapasitas = kapasitas,
        lokasi = lokasi
    )
}