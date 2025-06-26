package com.example.recehin.data.di

import android.content.Context
import com.example.recehin.data.local.SessionManager
import com.example.recehin.data.network.api.ApiConfig
import com.example.recehin.data.repository.AuthRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val sessionManager = SessionManager(context)
        return AuthRepository.getInstance(apiService, sessionManager)
    }
}