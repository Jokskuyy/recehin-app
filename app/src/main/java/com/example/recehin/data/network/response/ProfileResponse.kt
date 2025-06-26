// File: app/src/main/kotlin/com/example/recehin/data/network/response/ProfileResponse.kt
package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

// Wrapper terluar, tidak berubah
data class ProfileResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ProfileData // <-- Perubahan di sini, sekarang menunjuk ke ProfileData
)

// KELAS BARU untuk menangani objek "data" yang berisi "user"
data class ProfileData(
    @SerializedName("user")
    val user: UserProfile
)

// Kelas UserProfile, tidak berubah
data class UserProfile(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String?
)