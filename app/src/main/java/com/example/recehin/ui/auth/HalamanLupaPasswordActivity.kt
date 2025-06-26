package com.example.recehin.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.recehin.BaseActivity
import com.example.recehin.data.network.request.ForgotPasswordRequest
import com.example.recehin.databinding.ActivityLupaPasswordBinding // Ganti sesuai nama file layout Anda
import com.example.recehin.ui.ViewModelFactory

class HalamanLupaPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityLupaPasswordBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rootView = binding.root

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnReset.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                binding.tilEmail.error = "Email tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilEmail.error = null
            }

            val request = ForgotPasswordRequest(email)
            viewModel.forgotPassword(request)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.forgotPasswordResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                if(response.success) {
                    // Jika sukses, tutup halaman ini
                    finish()
                }
            }.onFailure {
                Toast.makeText(this, "Terjadi kesalahan: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
