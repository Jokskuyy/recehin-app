// File: app/src/main/kotlin/com/example/recehin/data/network/response/CategoryResponse.kt
package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

// Wrapper untuk response list kategori
data class CategoryResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Category>
)

// Model untuk satu item kategori
data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String // "income" or "expense"
)