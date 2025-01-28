package com.example.uas.ui.viewmodel.monitoring

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas.model.Hewan
import com.example.uas.model.Monitoring
import com.example.uas.repository.HewanRepository
import com.example.uas.repository.MonitoringRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiStateMonitoring {
    data class Success(val monitoring : List<Monitoring>) : HomeUiStateMonitoring()
    object Error : HomeUiStateMonitoring()
    object Loading : HomeUiStateMonitoring()
}

class HomeViewModelMonitoring(private val mnt : MonitoringRepository) : ViewModel(){
    var mntUiState: HomeUiStateMonitoring by mutableStateOf(HomeUiStateMonitoring.Loading)
        private set

    init {
        getMnt()
    }

    fun getMnt(){
        viewModelScope.launch {
            mntUiState = HomeUiStateMonitoring.Loading
            mntUiState = try {
                HomeUiStateMonitoring.Success(mnt.getMonitoring().data)
            } catch (e: IOException) {
                HomeUiStateMonitoring.Error
            } catch (e: HttpException) {
                HomeUiStateMonitoring.Error
            }
        }
    }
}