package com.example.recehin.ui.auth

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.recehin.R
import com.example.recehin.data.network.request.ForgotPasswordRequest
import com.example.recehin.databinding.ActivityLupaPasswordBinding
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.utils.BaseActivity

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

            // Membuat objek request sebelum memanggil ViewModel
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
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                // Mengambil token dari struktur data yang benar: response.data?.resetToken
                val token = response.data?.resetToken

                if(response.success && !token.isNullOrEmpty()) {
                    showTokenDialog(token)
                }
            }.onFailure {
                Toast.makeText(this, "Terjadi kesalahan: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Fungsi untuk menampilkan dialog yang berisi token.
     */
    private fun showTokenDialog(token: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_show_token, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Lanjut") { _, _ ->
                // Arahkan ke Halaman Reset Password saat tombol "Lanjut" ditekan
                val intent = Intent(this, HalamanResetPasswordActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tutup", null)
            .create()

        val tvToken: TextView = dialogView.findViewById(R.id.tv_token_value)
        val btnCopy: Button = dialogView.findViewById(R.id.btn_copy_token)

        tvToken.text = token

        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("token", token)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Token disalin ke clipboard!", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}
