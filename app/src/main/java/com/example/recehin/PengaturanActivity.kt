package com.example.recehin

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PengaturanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        rootView = findViewById(R.id.root_layout)

        // Tombol untuk kembali
        val btnKembali = findViewById<ImageButton>(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }
    }
}
