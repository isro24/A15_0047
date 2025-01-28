package com.example.uas.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.repository.KandangRepository
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertViewModelKandang(private val knd: KandangRepository): ViewModel(){
    var uiStateKandang by mutableStateOf(InsertUiStateKandang())
        private set

    fun handleNavigateBack(
        systemUiController: SystemUiController,
        onNavigateBack: () -> Unit
    ) {
        systemUiController.setStatusBarColor(Color.Transparent)

        onNavigateBack()
    }

    fun updateInsertKndState(insertUiEventKandang: InsertUiEventKandang){
        uiStateKandang = InsertUiStateKandang(insertUiEventKandang=insertUiEventKandang)
    }

    private fun hapusErrorState(){
        viewModelScope.launch {
            delay(5000)
            uiStateKandang = uiStateKandang.copy(isEntryValidKandang = FormErrorStateKandang())
        }
    }

    suspend fun insertKnd(){
        if (validateFields()){
            viewModelScope.launch {
                try {
                    knd.insertKandang(uiStateKandang.insertUiEventKandang.toKnd())
                    uiStateKandang = InsertUiStateKandang(
                        snackBarMessage = "Data Kandang berhasil ditambahkan",
                        insertUiEventKandang = InsertUiEventKandang(),
                        isEntryValidKandang = FormErrorStateKandang()
                    )
                }catch (e:Exception){
                    uiStateKandang = uiStateKandang.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        }
        else{
            uiStateKandang = uiStateKandang.copy(
                snackBarMessage = "Input tidak valid, periksa data anda kembali"
            )
        }
    }


    fun resetSnackBarMessage() {
        uiStateKandang = uiStateKandang.copy(snackBarMessage = null)
    }

    fun validateFields(): Boolean{
        val event = uiStateKandang.insertUiEventKandang
        val errorState = FormErrorStateKandang(
            idKandangError = if (event.idKandang.isNotEmpty()) null else "Id Kandang tidak boleh kosong",
            idHewanError = if (event.idHewan != null) null else "Id Hewan tidak boleh kosong",
            kapasitasError = if (event.kapasitas != null) null else "Kapasitas tidak boleh kosong",
            lokasiError = if (event.lokasi.isNotEmpty()) null else "Lokasi tidak boleh kosong",
        )
        uiStateKandang = uiStateKandang.copy(isEntryValidKandang = errorState)

        if (!errorState.isValid()){
            hapusErrorState()
        }
        return errorState.isValid()
    }

}

data class InsertUiStateKandang(
    val insertUiEventKandang: InsertUiEventKandang = InsertUiEventKandang(),
    val isEntryValidKandang: FormErrorStateKandang = FormErrorStateKandang(),
    val snackBarMessage: String? = null
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
    insertUiEventKandang = toInsertUiEventKandang(),
)

fun Kandang.toInsertUiEventKandang(): InsertUiEventKandang = InsertUiEventKandang(
    idKandang = idKandang,
    idHewan=idHewan,
    kapasitas = kapasitas,
    lokasi = lokasi
)

data class FormErrorStateKandang(
    val idKandangError: String? = null,
    val idHewanError: String? = null,
    val kapasitasError: String? = null,
    val lokasiError: String? = null,
){
    fun isValid(): Boolean{
        return idKandangError == null
                &&idHewanError ==  null
                &&kapasitasError == null
                &&lokasiError == null
    }
}

