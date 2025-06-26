package com.example.recehin.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recehin.data.di.Injection
import com.example.recehin.data.repository.AuthRepository
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.auth.AuthViewModel

/**
 * ViewModelFactory ini sekarang lebih kuat, karena ia bisa membuat
 * semua ViewModel yang ada di aplikasi dengan menyediakan repository yang sesuai.
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Untuk halaman Login/Register
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
            // Untuk halaman Beranda
            modelClass.isAssignableFrom(BerandaViewModel::class.java) -> {
                BerandaViewModel(transactionRepository) as T
            }
            // Untuk halaman Daftar Transaksi
            modelClass.isAssignableFrom(DaftarTransaksiViewModel::class.java) -> {
                DaftarTransaksiViewModel(transactionRepository) as T
            }
            // Untuk halaman Profil
            modelClass.isAssignableFrom(ProfilViewModel::class.java) -> {
                ProfilViewModel(transactionRepository) as T
            }
            // Logika untuk DaftarTagihanViewModel sengaja dihapus untuk saat ini
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                // Sekarang kita panggil kedua repository dari Injection
                INSTANCE ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideTransactionRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}
