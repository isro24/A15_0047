package com.example.uas.repository

import com.example.uas.model.AllKandangResponse
import com.example.uas.model.Kandang
import com.example.uas.model.KandangDetailResponse
import com.example.uas.service_api.KandangService
import java.io.IOException

interface KandangRepository{
    suspend fun insertKandang(kandang: Kandang)

    suspend fun getKandang(): AllKandangResponse

    suspend fun updateKandang(idKandang: String, kandang: Kandang)

    suspend fun deleteKandang(idKandang: String)

    suspend fun getKandangById(idKandang: String): KandangDetailResponse
}

class NetworkKandangRepository(
    private val kandangApiService: KandangService
): KandangRepository{
    override suspend fun insertKandang(kandang: Kandang) {
        kandangApiService.insertKandang(kandang)
    }

    override suspend fun updateKandang(idKandang: String, kandang: Kandang) {
        kandangApiService.updateKandang(idKandang, kandang)
    }

    override suspend fun deleteKandang(idKandang: String) {
        try {
            val response = kandangApiService.deleteKandang(idKandang)
            if (!response.isSuccessful){
                throw IOException("Failed to delete Kandang. HTTP Status code: " +
                        "${response.code()}")
            }else{
                response.message()
                println(response.message())
            }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getKandang(): AllKandangResponse =
        kandangApiService.getAllKandang()

    override suspend fun getKandangById(idKandang: String): KandangDetailResponse {
        return kandangApiService.getKandangById(idKandang)
    }
}