package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ResetTokenData?
)

data class ResetTokenData(
    @field:SerializedName("resetToken")
    val resetToken: String
)