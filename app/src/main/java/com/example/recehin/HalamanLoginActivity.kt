package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HalamanLoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        tilEmail = findViewById(R.id.til_email)
        etEmail = findViewById(R.id.et_email)
        tilPassword = findViewById(R.id.til_password)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)

        // Set click listeners
        btnLogin.setOnClickListener {
            // Validate input
            if (validateInput()) {
                // Navigate to home
                startActivity(Intent(this, BerandaActivity::class.java))
                finish()
            }
        }

        tvRegister.setOnClickListener {
            // Navigate to register
            startActivity(Intent(this, HalamanRegisterActivity::class.java))
            finish()
        }

        tvForgotPassword.setOnClickListener {
            // Navigate to forgot password
            startActivity(Intent(this, HalamanLupaPasswordActivity::class.java))
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

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

        return isValid
    }
}