// File: app/src/main/kotlin/com/example/recehin/data/network/request/AddTransactionRequest.kt
package com.example.recehin.data.network.request

import com.google.gson.annotations.SerializedName

data class AddTransactionRequest(
    @SerializedName("amount")
    val amount: Double,

    // --- PERUBAHAN FINAL DI SINI ---
    // Diubah dari "categoryID" menjadi "categoryId" agar cocok dengan pesan error server
    @SerializedName("categoryId")
    val categoryId: Int,

    @SerializedName("description")
    val description: String?,

    // Ini sudah benar dari perbaikan sebelumnya
    @SerializedName("transactionDate")
    val transactionDate: String
)