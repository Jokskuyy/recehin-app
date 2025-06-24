package com.example.recehin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HalamanLupaPasswordActivity : BaseActivity() {

    private lateinit var ibBack: ImageButton
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)

        rootView = findViewById(R.id.root_layout)

        // Initialize views
        ibBack = findViewById(R.id.ib_back)
        tilEmail = findViewById(R.id.til_email)
        etEmail = findViewById(R.id.et_email)
        btnReset = findViewById(R.id.btn_reset)

        // Set click listeners
        ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnReset.setOnClickListener {
            // Validate input
            if (validateInput()) {
                // Show success message
                Toast.makeText(
                    this,
                    "Link reset password telah dikirim ke email Anda",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun validateInput(): Boolean {
        // Validate email
        if (etEmail.text.toString().trim().isEmpty()) {
            tilEmail.error = "Email tidak boleh kosong"
            return false
        } else {
            tilEmail.error = null
        }
        return true
    }
}