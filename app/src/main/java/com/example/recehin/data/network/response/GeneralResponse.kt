// File: app/src/main/kotlin/com/example/recehin/data/network/response/GeneralResponse.kt
package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String
)