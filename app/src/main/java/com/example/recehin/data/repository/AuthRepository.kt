package com.example.recehin.data.repository

import com.example.recehin.data.local.SessionManager
import com.example.recehin.data.network.api.ApiService
import com.example.recehin.data.network.request.*
import com.example.recehin.data.network.response.GeneralResponse

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun register(registerRequest: RegisterRequest) = apiService.register(registerRequest)
    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) = apiService.forgotPassword(forgotPasswordRequest)

    fun saveToken(token: String) {
        sessionManager.saveAuthToken("Bearer $token")
    }

    fun clearToken() {
        sessionManager.clearAuthToken()
    }
    suspend fun resetPassword(request: ResetPasswordRequest): GeneralResponse {
        return apiService.resetPassword(request)
    }
    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            sessionManager: SessionManager
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, sessionManager).also { instance = it }
            }
    }
}