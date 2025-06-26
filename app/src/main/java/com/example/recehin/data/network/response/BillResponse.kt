// File: app/src/main/kotlin/com/example/recehin/data/network/response/BillResponse.kt
package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

data class BillResponse(
    @SerializedName("success")
    val success: Boolean,

    // Pastikan properti 'data' ini ada di file Anda
    @SerializedName("data")
    val data: List<Bill>
)