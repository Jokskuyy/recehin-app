package com.example.recehin.data.network.request

import com.google.gson.annotations.SerializedName

data class AddBillRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("categoryId")
    val categoryId: Int,

    // --- PERUBAHAN DI SINI ---
    // Nama field diubah dari "dueDate" menjadi "nextDueDate" agar cocok dengan backend
    @SerializedName("nextDueDate")
    val nextDueDate: String,

    @SerializedName("frequency")
    val frequency: String,

    @SerializedName("notification")
    val notification: Boolean
)
