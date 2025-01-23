package com.example.uas.ui.viewmodel.kandang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Kandang
import com.example.uas.repository.KandangRepository
import kotlinx.coroutines.launch

class InsertViewModelKandang(private val knd: KandangRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiStateKandang())
        private set

    fun updateInsertKndState(insertUiEventKandang: InsertUiEventKandang){
        uiState = InsertUiStateKandang(insertUiEventKandang=insertUiEventKandang)
    }

    suspend fun insertKnd(){
        viewModelScope.launch {
            try {
                knd.insertKandang(uiState.insertUiEventKandang.toKnd())
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiStateKandang(
    val insertUiEventKandang: InsertUiEventKandang = InsertUiEventKandang()
)

data class InsertUiEventKandang(
    val idKandang:String="",
    val idHewan:Int=0,
    val kapasitas:String="",
    val lokasi:String="",
)

fun InsertUiEventKandang.toKnd(): Kandang = Kandang(
    idKandang = idKandang,
    idHewan = idHewan,
    kapasitas = kapasitas,
    lokasi = lokasi,
)

fun Kandang.toUiStateKnd(): InsertUiStateKandang = InsertUiStateKandang(
    insertUiEventKandang = toInsertUiEventKandang()
)

fun Kandang.toInsertUiEventKandang(): InsertUiEventKandang = InsertUiEventKandang(
    idKandang = idKandang,
    idHewan=idHewan,
    kapasitas = kapasitas,
    lokasi = lokasi
)

