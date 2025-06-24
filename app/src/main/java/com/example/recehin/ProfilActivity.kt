package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilActivity : BaseActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        rootView = findViewById(R.id.root_layout)

        // Initialize views
        btnBack = findViewById(R.id.btn_back)
        ivAvatar = findViewById(R.id.iv_avatar)
        tvNama = findViewById(R.id.tv_nama)
        tvEmail = findViewById(R.id.tv_email)
        bottomNavigation = findViewById(R.id.bottom_navigation)



        // Back button action
        btnBack.setOnClickListener {
            finish()
        }

        // Setup bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    true
                }
                R.id.nav_transaksi -> {
                    startActivity(Intent(this, DaftarTransaksiActivity::class.java))
                    true
                }
                R.id.nav_pengaturan -> {
                    startActivity(Intent(this, PengaturanActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Highlight the current page
    }
}
