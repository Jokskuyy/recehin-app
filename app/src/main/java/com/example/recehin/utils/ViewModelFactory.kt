// File: app/src/main/kotlin/com/example/recehin/ui/utils/ViewModelFactory.kt
package com.example.recehin.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recehin.data.repository.AuthRepository
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.BerandaViewModel
import com.example.recehin.ui.DaftarTransaksiViewModel

/**
 * Kelas ini bertanggung jawab untuk membuat instance dari semua ViewModel di aplikasi.
 * Ini memungkinkan kita untuk memasukkan 'repository' sebagai parameter ke dalam ViewModel.
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Blok 'when' ini adalah inti dari factory.
        // Ia memeriksa ViewModel mana yang diminta dan membuatnya.
        return when {
            // Jika yang diminta adalah BerandaViewModel...
            modelClass.isAssignableFrom(BerandaViewModel::class.java) -> {
                BerandaViewModel(transactionRepository) as T
            }
            // Jika yang diminta adalah DaftarTransaksiViewModel...
            modelClass.isAssignableFrom(DaftarTransaksiViewModel::class.java) -> {
                DaftarTransaksiViewModel(transactionRepository) as T
            }
            // Jika ViewModel yang diminta tidak dikenal, lempar error.
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}