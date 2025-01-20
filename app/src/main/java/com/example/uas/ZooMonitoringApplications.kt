package com.example.uas

import android.app.Application
import com.example.uas.di.AppContainerHewan
import com.example.uas.di.AppContainerKandang
import com.example.uas.di.AppContainerMonitoring
import com.example.uas.di.AppContainerPetugas
import com.example.uas.di.HewanContainer
import com.example.uas.di.KandangContainer
import com.example.uas.di.MonitoringContainer
import com.example.uas.di.PetugasContainer

class ZooMonitoringApplications:Application() {
    lateinit var containerHewan: AppContainerHewan
    lateinit var containerKandang: AppContainerKandang
    lateinit var containerPetugas: AppContainerPetugas
    lateinit var containerMonitoring: AppContainerMonitoring

    override fun onCreate() {
        super.onCreate()
        containerHewan= HewanContainer()
        containerKandang= KandangContainer()
        containerPetugas= PetugasContainer()
        containerMonitoring= MonitoringContainer()
    }
}