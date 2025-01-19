package com.example.uas.di

import com.example.uas.repository.KandangRepository
import com.example.uas.repository.NetworkKandangRepository
import com.example.uas.repository.NetworkPetugasRepository
import com.example.uas.repository.PetugasRepository
import com.example.uas.service_api.KandangService
import com.example.uas.service_api.PetugasService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainerPetugas{
    val petugasRepository: PetugasRepository
}

class PetugasContainer: AppContainerPetugas{
    private val baseUrl= "http://10.0.2.2:3000/api/petugas/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val petugasService: PetugasService by lazy {
        retrofit.create(PetugasService::class.java)
    }

    override val petugasRepository: PetugasRepository by lazy {
        NetworkPetugasRepository(petugasService)
    }
}