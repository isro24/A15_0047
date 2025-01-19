package com.example.uas.service_api

import com.example.uas.model.AllHewanResponse
import com.example.uas.model.AllPetugasResponse
import com.example.uas.model.Hewan
import com.example.uas.model.HewanDetailResponse
import com.example.uas.model.Petugas
import com.example.uas.model.PetugasDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PetugasService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @POST("store")
    suspend fun insertPetugas(@Body petugas: Petugas)

    @GET(".")
    suspend fun getAllPetugas(): AllPetugasResponse

    @GET("{id_petugas}")
    suspend fun getPetugasById(@Path("id_petugas") idPetugas:String): PetugasDetailResponse

    @PUT("{id_petugas}")
    suspend fun updatePetugas(@Path("id_petugas") idPetugas:String, @Body petugas: Petugas)

    @DELETE("{id_petugas}")
    suspend fun deletePetugas(@Path("id_petugas") idPetugas:String): Response<Void>
}