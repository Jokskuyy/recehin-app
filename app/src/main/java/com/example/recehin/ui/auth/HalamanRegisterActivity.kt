package com.example.recehin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.recehin.BaseActivity
import com.example.recehin.data.network.request.RegisterRequest
import com.example.recehin.databinding.ActivityRegisterBinding // Ganti sesuai nama file layout Anda
import com.example.recehin.ui.ViewModelFactory

class HalamanRegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rootView = binding.root

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            if (validateInput()) {
                val name = binding.etNama.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString()

                // Memanggil fungsi register di ViewModel
                val request = RegisterRequest(name, email, password)
                viewModel.register(request)
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, HalamanLoginActivity::class.java))
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                if (response.success) {
                    // Jika register berhasil, arahkan ke halaman Login
                    val intent = Intent(this, HalamanLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.onFailure {
                Toast.makeText(this, "Terjadi kesalahan: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Validasi lokal tetap penting untuk user experience yang baik
    private fun validateInput(): Boolean {
        var isValid = true
        binding.tilNama.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilKonfirmasiPassword.error = null

        if (binding.etNama.text.toString().trim().isEmpty()) {
            binding.tilNama.error = "Nama tidak boleh kosong"
            isValid = false
        }

        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.tilEmail.error = "Email tidak boleh kosong"
            isValid = false
        }

        if (binding.etPassword.text.toString().isEmpty()) {
            binding.tilPassword.error = "Password tidak boleh kosong"
            isValid = false
        }

        if (binding.etKonfirmasiPassword.text.toString() != binding.etPassword.text.toString()) {
            binding.tilKonfirmasiPassword.error = "Password tidak cocok"
            isValid = false
        }

        return isValid
    }
}