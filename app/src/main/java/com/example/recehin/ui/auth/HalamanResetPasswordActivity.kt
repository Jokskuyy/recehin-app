package com.example.recehin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.recehin.data.network.request.ResetPasswordRequest
import com.example.recehin.databinding.ActivityResetPasswordBinding
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.utils.BaseActivity

class HalamanResetPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rootView = binding.root

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSavePassword.setOnClickListener {
            val token = binding.etToken.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            var isValid = true
            if (token.isEmpty()) {
                binding.tilToken.error = "Kode reset tidak boleh kosong"; isValid = false
            } else { binding.tilToken.error = null }

            if (newPassword.length < 8) {
                binding.tilNewPassword.error = "Password minimal 8 karakter"; isValid = false
            } else { binding.tilNewPassword.error = null }

            if (newPassword != confirmPassword) {
                binding.tilConfirmPassword.error = "Password tidak cocok"; isValid = false
            } else { binding.tilConfirmPassword.error = null }

            if (!isValid) return@setOnClickListener

            val request = ResetPasswordRequest(token, newPassword)
            viewModel.resetPassword(request)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.resetPasswordResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()

                // --- BAGIAN INI YANG MEMASTIKAN PINDAH HALAMAN ---
                if (response.status == "success") {
                    val intent = Intent(this, HalamanLoginActivity::class.java)
                    // Membersihkan tumpukan activity agar tidak bisa kembali ke halaman reset
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }.onFailure {
                Toast.makeText(this, "Gagal: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
