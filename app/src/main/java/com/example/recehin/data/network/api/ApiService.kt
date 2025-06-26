package com.example.recehin.data.network.api

import com.example.recehin.data.network.request.*
import com.example.recehin.data.network.response.*
import retrofit2.http.Body
import retrofit2.http.*
import com.example.recehin.data.network.request.AddBillRequest

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): AuthResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): AuthResponse

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): ForgotPasswordResponse

    // --- TAMBAHKAN FUNGSI INI ---
    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): GeneralResponse

    @GET("api/auth/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    @GET("api/transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
        @Query("type") type: String? = null,
        @Query("search") search: String? = null
    ): TransactionListResponse

    @POST("api/transactions")
    suspend fun addTransaction(
        @Header("Authorization") token: String,
        @Body request: AddTransactionRequest
    ): GeneralResponse

    @GET("api/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String,
        @Query("type") type: String
    ): CategoryResponse

    // --- BAGIAN YANG DIPERBAIKI ---
    @GET("api/bills")
    suspend fun getBills(@Header("Authorization") token: String): BillResponse // Diubah dari Bill menjadi BillResponse
    // --- AKHIR BAGIAN YANG DIPERBAIKI ---

    @POST("api/bills")
    suspend fun addBill(
        @Header("Authorization") token: String,
        @Body request: AddBillRequest
    ): GeneralResponse

}
