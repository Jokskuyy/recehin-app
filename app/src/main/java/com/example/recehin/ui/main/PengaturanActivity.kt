package com.example.recehin.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.recehin.utils.BaseActivity
import com.example.recehin.databinding.ActivityPengaturanBinding // Menggunakan ViewBinding
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.ui.auth.AuthViewModel
import com.example.recehin.ui.auth.HalamanLoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PengaturanActivity : BaseActivity() {

    private lateinit var binding: ActivityPengaturanBinding
    // Kita tetap menggunakan AuthViewModel karena fungsi logout ada di sana
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rootView = binding.root

        setupListeners()
    }

    private fun setupListeners() {
        // Tombol untuk kembali
        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Listener untuk tombol keluar
        binding.btnKeluarAkun.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Konfirmasi Keluar")
            .setMessage("Apakah Anda yakin ingin keluar dari akun Anda?")
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Keluar") { dialog, _ ->
                // Panggil fungsi logout di ViewModel
                viewModel.logout()
                Toast.makeText(this, "Anda telah keluar.", Toast.LENGTH_SHORT).show()

                // Arahkan ke Halaman Login dan hapus semua activity sebelumnya
                val intent = Intent(this, HalamanLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .show()
    }
}