package com.example.uas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllPetugasResponse (
    val status : Boolean,
    val message : String,
    val data: List<Petugas>
)

@Serializable
data class PetugasDetailResponse(
    val status : Boolean,
    val message : String,
    val data: Petugas
)
@Serializable
data class Petugas (
    @SerialName("id_petugas")
    val idPetugas: Int,
    @SerialName("nama_petugas")
    val namaPetugas: String,
    val jabatan: String,
)