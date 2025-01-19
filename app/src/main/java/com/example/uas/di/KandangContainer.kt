package com.example.uas.di

import com.example.uas.repository.HewanRepository
import com.example.uas.repository.KandangRepository
import com.example.uas.repository.NetworkHewanRepository
import com.example.uas.repository.NetworkKandangRepository
import com.example.uas.service_api.HewanService
import com.example.uas.service_api.KandangService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainerKandang{
    val kandangRepository: KandangRepository
}

class KandangContainer: AppContainerKandang{
    private val baseUrl= "http://10.0.2.2:3000/api/kandang/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val kandangService: KandangService by lazy {
        retrofit.create(KandangService::class.java)
    }

    override val kandangRepository: KandangRepository by lazy {
        NetworkKandangRepository(kandangService)
    }
}