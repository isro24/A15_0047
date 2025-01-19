package com.example.uas.repository

import com.example.uas.model.AllHewanResponse
import com.example.uas.model.Hewan
import com.example.uas.model.HewanDetailResponse
import com.example.uas.service_api.HewanService
import java.io.IOException

interface HewanRepository{
    suspend fun insertHewan(hewan: Hewan)

    suspend fun getHewan(): AllHewanResponse

    suspend fun updateHewan(idHewan: String, hewan: Hewan)

    suspend fun deleteHewan(idHewan: String)

    suspend fun getHewanById(idHewan: String): HewanDetailResponse
}

class NetworkHewanRepository(
    private val hewanApiService: HewanService
): HewanRepository{
    override suspend fun insertHewan(hewan: Hewan) {
        hewanApiService.insertHewan(hewan)
    }

    override suspend fun updateHewan(idHewan: String, hewan: Hewan) {
        hewanApiService.updateHewan(idHewan, hewan)
    }

    override suspend fun deleteHewan(idHewan: String) {
        try {
            val response = hewanApiService.deleteHewan(idHewan)
            if (!response.isSuccessful){
                throw IOException("Failed to delete Hewan. HTTP Status code: " +
                        "${response.code()}")
            }else{
                response.message()
                println(response.message())
            }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getHewan(): AllHewanResponse=
        hewanApiService.getAllHewan()

    override suspend fun getHewanById(idHewan: String): HewanDetailResponse {
        return hewanApiService.getHewanById(idHewan)
    }
}