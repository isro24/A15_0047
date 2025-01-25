package com.example.uas.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uas.ZooMonitoringApplications
import com.example.uas.ui.viewmodel.hewan.DetailViewModel
import com.example.uas.ui.viewmodel.hewan.HomeViewModel
import com.example.uas.ui.viewmodel.hewan.InsertViewModel
import com.example.uas.ui.viewmodel.hewan.UpdateViewModel
import com.example.uas.ui.viewmodel.kandang.DetailViewModelKandang
import com.example.uas.ui.viewmodel.kandang.HomeViewModelKandang
import com.example.uas.ui.viewmodel.kandang.InsertViewModelKandang
import com.example.uas.ui.viewmodel.kandang.UpdateViewModelKandang
import com.example.uas.ui.viewmodel.monitoring.DetailViewModelMonitoring
import com.example.uas.ui.viewmodel.monitoring.HomeViewModelMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertViewModelMonitoring
import com.example.uas.ui.viewmodel.monitoring.UpdateViewModelMonitoring
import com.example.uas.ui.viewmodel.petugas.DetailViewModelPetugas
import com.example.uas.ui.viewmodel.petugas.HomeViewModelPetugas
import com.example.uas.ui.viewmodel.petugas.InsertViewModelPetugas
import com.example.uas.ui.viewmodel.petugas.UpdateViewModelPetugas

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(aplikasiZoo().containerHewan.hewanRepository) }
        initializer { InsertViewModel(aplikasiZoo().containerHewan.hewanRepository) }
        initializer { DetailViewModel(createSavedStateHandle(),aplikasiZoo().containerHewan.hewanRepository) }
        initializer { UpdateViewModel(createSavedStateHandle(),aplikasiZoo().containerHewan.hewanRepository) }

        initializer { HomeViewModelPetugas(aplikasiZoo().containerPetugas.petugasRepository) }
        initializer { InsertViewModelPetugas(aplikasiZoo().containerPetugas.petugasRepository) }
        initializer { DetailViewModelPetugas(createSavedStateHandle(),aplikasiZoo().containerPetugas.petugasRepository) }
        initializer { UpdateViewModelPetugas(createSavedStateHandle(),aplikasiZoo().containerPetugas.petugasRepository) }

        initializer { HomeViewModelKandang(aplikasiZoo().containerKandang.kandangRepository) }
        initializer { InsertViewModelKandang(aplikasiZoo().containerKandang.kandangRepository) }
        initializer { DetailViewModelKandang(createSavedStateHandle(),aplikasiZoo().containerKandang.kandangRepository) }
        initializer { UpdateViewModelKandang(createSavedStateHandle(),aplikasiZoo().containerKandang.kandangRepository) }

        initializer { HomeViewModelMonitoring(aplikasiZoo().containerMonitoring.monitoringRepository) }
        initializer { InsertViewModelMonitoring(aplikasiZoo().containerMonitoring.monitoringRepository) }
        initializer { DetailViewModelMonitoring(createSavedStateHandle(),aplikasiZoo().containerMonitoring.monitoringRepository) }
        initializer { UpdateViewModelMonitoring(createSavedStateHandle(),aplikasiZoo().containerMonitoring.monitoringRepository) }
    }
}

fun CreationExtras.aplikasiZoo(): ZooMonitoringApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ZooMonitoringApplications)