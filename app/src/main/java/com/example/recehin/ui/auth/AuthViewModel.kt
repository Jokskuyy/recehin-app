package com.example.recehin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recehin.data.network.request.*
import com.example.recehin.data.network.response.*
import com.example.recehin.data.repository.AuthRepository
import com.example.recehin.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<AuthResponse>>()
    val registerResult: LiveData<Result<AuthResponse>> = _registerResult

    private val _loginResult = MutableLiveData<Result<AuthResponse>>()
    val loginResult: LiveData<Result<AuthResponse>> = _loginResult

    private val _forgotPasswordResult = MutableLiveData<Result<ForgotPasswordResponse>>()
    val forgotPasswordResult: LiveData<Result<ForgotPasswordResponse>> = _forgotPasswordResult

    // --- LIVE DATA BARU UNTUK HASIL RESET ---
    private val _resetPasswordResult = SingleLiveEvent<Result<GeneralResponse>>()
    val resetPasswordResult: LiveData<Result<GeneralResponse>> = _resetPasswordResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(registerRequest: RegisterRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(registerRequest)
                _registerResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _registerResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(loginRequest: LoginRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest)
                _loginResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.forgotPassword(forgotPasswordRequest)
                _forgotPasswordResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _forgotPasswordResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FUNGSI BARU UNTUK MENJALANKAN RESET PASSWORD ---
    fun resetPassword(request: ResetPasswordRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.resetPassword(request)
                _resetPasswordResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _resetPasswordResult.postValue(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveToken(token: String) {
        repository.saveToken(token)
    }

    fun logout() {
        repository.clearToken()
    }
}
