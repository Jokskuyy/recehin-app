// File: app/src/main/kotlin/com/example/recehin/data/repository/TransactionRepository.kt
package com.example.recehin.data.repository

import com.example.recehin.data.local.SessionManager
import com.example.recehin.data.network.api.ApiService
import com.example.recehin.data.network.request.AddTransactionRequest
import com.example.recehin.data.network.response.*
import com.example.recehin.data.network.request.AddBillRequest
import com.example.recehin.data.network.response.Bill

class TransactionRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private fun getAuthToken(): String {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("Auth token is missing!")
        }
        return if (token.startsWith("Bearer ", ignoreCase = true)) {
            token
        } else {
            "Bearer $token"
        }
    }

    suspend fun getProfile(): UserProfile {
        return apiService.getProfile(getAuthToken()).data.user
    }

    suspend fun getAllTransactions(): List<Transaction> {
        return apiService.getTransactions(getAuthToken()).data
    }

    suspend fun getCategories(type: String): List<Category> {
        return apiService.getCategories(getAuthToken(), type).data
    }

    suspend fun addTransaction(request: AddTransactionRequest): GeneralResponse {
        return apiService.addTransaction(getAuthToken(), request)
    }

    suspend fun getBills(): List<Bill> {
        return apiService.getBills(getAuthToken()).data
    }

    suspend fun addBill(request: AddBillRequest): GeneralResponse {
        return apiService.addBill(getAuthToken(), request)
    }

    companion object {
        @Volatile
        private var instance: TransactionRepository? = null

        fun getInstance(
            apiService: ApiService,
            sessionManager: SessionManager
        ): TransactionRepository =
            instance ?: synchronized(this) {
                instance ?: TransactionRepository(apiService, sessionManager)
            }.also { instance = it }
    }
}