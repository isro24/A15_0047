package com.example.uas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllHewanResponse (
    val status : Boolean,
    val message : String,
    val data: List<Hewan>
)

@Serializable
data class HewanDetailResponse(
    val status : Boolean,
    val message : String,
    val data: Hewan
)
@Serializable
data class Hewan (
    @SerialName("id_hewan")
    val idHewan: Int,
    @SerialName("nama_hewan")
    val namaHewan: String,
    @SerialName("tipe_pakan")
    val tipePakan: String,
    val populasi: String,
    @SerialName("zona_wilayah")
    val zonaWilayah: String,
)