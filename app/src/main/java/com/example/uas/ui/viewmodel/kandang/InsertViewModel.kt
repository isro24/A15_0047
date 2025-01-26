package com.example.uas.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.repository.KandangRepository
import kotlinx.coroutines.launch

class InsertViewModelKandang(private val knd: KandangRepository): ViewModel(){
    var uiState by mutableStateOf(InsertUiStateKandang())
        private set

    var listHewan by mutableStateOf<List<Hewan>>(emptyList())
        private set

    init {
        fetchHewan()
    }

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

    private fun fetchHewan() {
        viewModelScope.launch {
            try {
                val response = knd.getHewan()
                if (response.status) {
                    listHewan = response.data
                    Log.d("FetchHewan", "Data Hewan: $listHewan")
                }
            } catch (e: Exception) {
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
    val idHewan:Int? = null,
    val kapasitas: Int? = null,
    val lokasi:String="",
)

fun InsertUiEventKandang.toKnd(): Kandang = Kandang(
    idKandang = idKandang,
    idHewan = idHewan?: 0,
    kapasitas = kapasitas?: 0,
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

