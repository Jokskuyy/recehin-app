package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

data class Bill(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("amount")
    val amount: Double,

    // --- PERUBAHAN DI SINI ---
    // Nama field disesuaikan menjadi "nextDueDate" agar cocok dengan backend
    @SerializedName("next_due_date")
    val dueDate: String,

    @SerializedName("frequency")
    val frequency: String,

    // Asumsi backend juga mengirim category_name. Jika tidak, ini bisa jadi null.
    @SerializedName("category_name")
    val categoryName: String
)
