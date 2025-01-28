package com.example.uas.service_api

import com.example.uas.model.AllKandangResponse
import com.example.uas.model.Kandang
import com.example.uas.model.KandangDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KandangService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @POST("store")
    suspend fun insertKandang(@Body kandang: Kandang)

    @GET(".")
    suspend fun getAllKandang(): AllKandangResponse

    @GET("{id_kandang}")
    suspend fun getKandangById(@Path("id_kandang") idKandang:String): KandangDetailResponse

    @PUT("{id_kandang}")
    suspend fun updateKandang(@Path("id_kandang") idKandang:String, @Body kandang: Kandang)

    @DELETE("{id_kandang}")
    suspend fun deleteKandang(@Path("id_kandang")idKandang: String): Response<Void>
}