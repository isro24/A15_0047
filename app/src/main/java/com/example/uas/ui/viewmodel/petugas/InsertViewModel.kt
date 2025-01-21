package com.example.uas.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Petugas
import com.example.uas.repository.PetugasRepository
import kotlinx.coroutines.launch

class InsertViewModel(private val ptg: PetugasRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiStatePetugas())
        private set

    fun updateInsertPtgState(insertUiEventPetugas: InsertUiEventPetugas){
        uiState = InsertUiStatePetugas(insertUiEventPetugas=insertUiEventPetugas)
    }

    suspend fun insertMhs(){
        viewModelScope.launch {
            try {
                ptg.insertPetugas(uiState.insertUiEventPetugas.toPtg())
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiStatePetugas(
    val insertUiEventPetugas: InsertUiEventPetugas = InsertUiEventPetugas()
)

data class InsertUiEventPetugas(
    val idPetugas:Int=0,
    val namaPetugas:String="",
    val jabatan:String="",
)

fun InsertUiEventPetugas.toPtg(): Petugas = Petugas(
    idPetugas=idPetugas,
    namaPetugas=namaPetugas,
    jabatan=jabatan
)

fun Petugas.toUiStatePtg(): InsertUiStatePetugas = InsertUiStatePetugas(
    insertUiEventPetugas = toInsertUiEventPetugas()
)

fun Petugas.toInsertUiEventPetugas(): InsertUiEventPetugas = InsertUiEventPetugas(
    idPetugas=idPetugas,
    namaPetugas=namaPetugas,
    jabatan=jabatan
)