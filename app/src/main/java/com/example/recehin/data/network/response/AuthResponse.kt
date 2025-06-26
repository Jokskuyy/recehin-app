package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: AuthData?
)

data class AuthData(
    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("token")
    val token: String
)

data class User(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("phone")
    val phone: String?
)