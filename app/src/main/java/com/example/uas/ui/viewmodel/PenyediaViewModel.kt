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

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(aplikasiZoo().containerHewan.hewanRepository) }
        initializer { InsertViewModel(aplikasiZoo().containerHewan.hewanRepository) }
        initializer { DetailViewModel(createSavedStateHandle(),aplikasiZoo().containerHewan.hewanRepository) }
    }
}

fun CreationExtras.aplikasiZoo(): ZooMonitoringApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ZooMonitoringApplications)