package com.example.uas.ui.viewmodel.kandang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas.model.Kandang
import com.example.uas.repository.KandangRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiStateKandang {
    data class Success(val kandang : List<Kandang>) : HomeUiStateKandang()
    object Error : HomeUiStateKandang()
    object Loading : HomeUiStateKandang()
}

class HomeViewModelKandang(private val knd: KandangRepository) : ViewModel(){
    var kndUiState: HomeUiStateKandang by mutableStateOf(HomeUiStateKandang.Loading)
        private set

    init {
        getKnd()
    }

    fun getKnd(){
        viewModelScope.launch {
            kndUiState = HomeUiStateKandang.Loading
            kndUiState = try {
                HomeUiStateKandang.Success(knd.getKandang().data)
            } catch (e: IOException) {
                HomeUiStateKandang.Error
            } catch (e: HttpException) {
                HomeUiStateKandang.Error
            }
        }
    }
}