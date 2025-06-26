// File: app/src/main/kotlin/com/example/recehin/data/di/Injection.kt
package com.example.recehin.data.di

import android.content.Context
import com.example.recehin.data.local.SessionManager
import com.example.recehin.data.network.api.ApiConfig
import com.example.recehin.data.repository.AuthRepository
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.utils.ViewModelFactory

object Injection {

    // Fungsi untuk menyediakan AuthRepository (asumsi sudah ada)
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val sessionManager = SessionManager(context)
        return AuthRepository.getInstance(apiService, sessionManager)
    }

    // Fungsi untuk menyediakan TransactionRepository (BARU)
    fun provideTransactionRepository(context: Context): TransactionRepository {
        val apiService = ApiConfig.getApiService()
        val sessionManager = SessionManager(context)
        return TransactionRepository.getInstance(apiService, sessionManager)
    }

    // Fungsi untuk menyediakan ViewModelFactory (BARU)
    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val authRepository = provideAuthRepository(context)
        val transactionRepository = provideTransactionRepository(context)
        return ViewModelFactory(authRepository, transactionRepository)
    }
}