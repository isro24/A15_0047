package com.example.uas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllKandangResponse (
    val status : Boolean,
    val message : String,
    val data: List<Kandang>
)

@Serializable
data class KandangDetailResponse(
    val status : Boolean,
    val message : String,
    val data: Kandang
)
@Serializable
data class Kandang (
    @SerialName("id_kandang")
    val idKandang: String,
    @SerialName("id_hewan")
    val idHewan: Int,
    val kapasitas: String,
    val lokasi: String,
)