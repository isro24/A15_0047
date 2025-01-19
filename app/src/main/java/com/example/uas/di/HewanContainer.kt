package com.example.uas.di

import com.example.uas.repository.HewanRepository
import com.example.uas.repository.NetworkHewanRepository
import com.example.uas.service_api.HewanService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val hewanRepository: HewanRepository
}

class HewanContainer: AppContainer{
    private val baseUrl= "http://10.0.2.2:3000/api/hewan/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val hewanService: HewanService by lazy {
        retrofit.create(HewanService::class.java)
    }

    override val hewanRepository: HewanRepository by lazy {
        NetworkHewanRepository(hewanService)
    }
}