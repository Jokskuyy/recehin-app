package com.example.recehin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recehin.data.network.response.UserProfile
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class ProfilViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun fetchProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _userProfile.postValue(profile)
            } catch (e: Exception) {
                _toastMessage.postValue("Gagal memuat profil: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
