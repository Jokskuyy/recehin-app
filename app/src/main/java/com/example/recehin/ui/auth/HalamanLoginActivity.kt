package com.example.recehin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recehin.BaseActivity
import com.example.recehin.data.network.request.LoginRequest
import com.example.recehin.databinding.ActivityLoginBinding
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.ui.main.BerandaActivity

class HalamanLoginActivity : BaseActivity() {

    // Menggunakan ViewBinding untuk mengakses view
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Asumsi rootView ada di BaseActivity atau di layout Anda
        rootView = binding.root

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            // Validasi dasar di UI
            if (email.isEmpty()) {
                binding.tilEmail.error = "Email tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilEmail.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Password tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            // Memanggil fungsi login di ViewModel
            val request = LoginRequest(email, password)
            viewModel.login(request)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, HalamanRegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, HalamanLupaPasswordActivity::class.java))
        }
    }

    private fun setupObservers() {
        // Observer untuk status loading (menampilkan/menyembunyikan ProgressBar)
        viewModel.isLoading.observe(this) { isLoading ->
            // Pastikan Anda punya ProgressBar dengan id 'progressBar' di layout Anda
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observer untuk hasil login dari API
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.success && response.data != null) {
                    // Jika sukses, simpan token dan pindah ke Beranda
                    viewModel.saveToken(response.data.token)
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, BerandaActivity::class.java)
                    // Hapus semua activity sebelumnya dari back stack
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Tampilkan pesan error dari server jika login gagal
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            }.onFailure {
                // Tampilkan pesan jika terjadi error koneksi atau lainnya
                Toast.makeText(this, "Terjadi kesalahan: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}