package com.example.recehin

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PengaturanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        // Tombol untuk kembali
        val btnKembali = findViewById<ImageButton>(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }
    }
}
