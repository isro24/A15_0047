package com.example.uas.service_api

import com.example.uas.model.AllHewanResponse
import com.example.uas.model.Hewan
import com.example.uas.model.HewanDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HewanService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @POST("store")
    suspend fun insertHewan(@Body hewan: Hewan)

    @GET(".")
    suspend fun getAllHewan(): AllHewanResponse

    @GET("{id_hewan}")
    suspend fun getHewanById(@Path("id_hewan") idHewan:String): HewanDetailResponse

    @PUT("{id_hewan}")
    suspend fun updateHewan(@Path("id_hewan") idHewan:String, @Body hewan: Hewan)

    @DELETE("{id_hewan}")
    suspend fun deleteHewan(@Path("id_hewan")idHewan: String): Response<Void>
}