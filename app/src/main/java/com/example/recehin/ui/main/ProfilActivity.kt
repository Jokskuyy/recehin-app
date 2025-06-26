package com.example.recehin.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recehin.R
import com.example.recehin.ui.ProfilViewModel
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.utils.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilActivity : BaseActivity() {

    private lateinit var viewModel: ProfilViewModel
    private lateinit var btnBack: ImageButton
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        rootView = findViewById(R.id.root_layout)

        // Inisialisasi ViewModel dengan Factory yang benar
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfilViewModel::class.java]

        initViews()
        setupListeners()
        setupBottomNavigation()
        setupObservers()

        // Panggil ViewModel untuk memuat data profil
        viewModel.fetchProfile()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        tvNama = findViewById(R.id.tv_nama)
        tvEmail = findViewById(R.id.tv_email)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        progressBar = findViewById(R.id.progressBar_profil)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.toastMessage.observe(this) {
            if (!it.isNullOrEmpty()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.userProfile.observe(this) { userProfile ->
            // Set data dinamis ke TextView
            tvNama.text = userProfile.name
            tvEmail.text = userProfile.email
        }
    }

    private fun setupBottomNavigation() {
        // Logika untuk bottom navigation tidak berubah
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
    }
}
