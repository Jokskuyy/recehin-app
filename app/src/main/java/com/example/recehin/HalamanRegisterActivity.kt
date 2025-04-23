package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HalamanRegisterActivity : AppCompatActivity() {

    private lateinit var tilNama: TextInputLayout
    private lateinit var etNama: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilKonfirmasiPassword: TextInputLayout
    private lateinit var etKonfirmasiPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        tilNama = findViewById(R.id.til_nama)
        etNama = findViewById(R.id.et_nama)
        tilEmail = findViewById(R.id.til_email)
        etEmail = findViewById(R.id.et_email)
        tilPassword = findViewById(R.id.til_password)
        etPassword = findViewById(R.id.et_password)
        tilKonfirmasiPassword = findViewById(R.id.til_konfirmasi_password)
        etKonfirmasiPassword = findViewById(R.id.et_konfirmasi_password)
        btnRegister = findViewById(R.id.btn_register)
        tvLogin = findViewById(R.id.tv_login)

        // Set click listeners
        btnRegister.setOnClickListener {
            // Validate input
            if (validateInput()) {
                // Navigate to home
                startActivity(Intent(this, BerandaActivity::class.java))
                finish()
            }
        }

        tvLogin.setOnClickListener {
            // Navigate to login
            startActivity(Intent(this, HalamanLoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        // Validate nama
        if (etNama.text.toString().trim().isEmpty()) {
            tilNama.error = "Nama tidak boleh kosong"
            isValid = false
        } else {
            tilNama.error = null
        }

        // Validate email
        if (etEmail.text.toString().trim().isEmpty()) {
            tilEmail.error = "Email tidak boleh kosong"
            isValid = false
        } else {
            tilEmail.error = null
        }

        // Validate password
        if (etPassword.text.toString().trim().isEmpty()) {
            tilPassword.error = "Password tidak boleh kosong"
            isValid = false
        } else {
            tilPassword.error = null
        }

        // Validate konfirmasi password
        if (etKonfirmasiPassword.text.toString().trim().isEmpty()) {
            tilKonfirmasiPassword.error = "Konfirmasi password tidak boleh kosong"
            isValid = false
        } else if (etPassword.text.toString() != etKonfirmasiPassword.text.toString()) {
            tilKonfirmasiPassword.error = "Password tidak cocok"
            isValid = false
        } else {
            tilKonfirmasiPassword.error = null
        }

        return isValid
    }
}