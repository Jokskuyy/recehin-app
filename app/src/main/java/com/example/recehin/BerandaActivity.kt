package com.example.recehin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BerandaActivity : AppCompatActivity() {

    private lateinit var tvSaldoTotal: TextView
    private lateinit var tvPemasukan: TextView
    private lateinit var tvPengeluaran: TextView
    private lateinit var cvLihatTransaksi: CardView
    private lateinit var cvArtikel1: CardView
    private lateinit var cvArtikel2: CardView
    private lateinit var fabTambahTransaksi: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var ibNotifikasi: ImageButton
    private lateinit var ibProfil: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        // Initialize views
        tvSaldoTotal = findViewById(R.id.tv_saldo_total)
        tvPemasukan = findViewById(R.id.tv_pemasukan)
        tvPengeluaran = findViewById(R.id.tv_pengeluaran)
        cvLihatTransaksi = findViewById(R.id.cv_lihat_transaksi)
        cvArtikel1 = findViewById(R.id.cv_artikel_1)
        cvArtikel2 = findViewById(R.id.cv_artikel_2)
        fabTambahTransaksi = findViewById(R.id.fab_tambah_transaksi)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        ibNotifikasi = findViewById(R.id.ib_notifikasi)
        ibProfil = findViewById(R.id.ib_profil)

        // Set dummy data for static UI
        tvSaldoTotal.text = "Rp 2.500.000"
        tvPemasukan.text = "Rp 4.000.000"
        tvPengeluaran.text = "Rp 1.500.000"

        // Set click listeners
        cvLihatTransaksi.setOnClickListener {
            startActivity(Intent(this, DaftarTransaksiActivity::class.java))
        }

//        fabTambahTransaksi.setOnClickListener {
//            startActivity(Intent(this, InputTransaksiActivity::class.java))
//        }

        ibProfil.setOnClickListener {
            startActivity(Intent(this, PengaturanActivity::class.java))
        }

        // Setup bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_transaksi -> {
                    startActivity(Intent(this, DaftarTransaksiActivity::class.java))
                    true
                }
//                R.id.nav_analisis -> {
//                    startActivity(Intent(this, AnalisisKeuanganActivity::class.java))
//                    true
//                }
                R.id.nav_pengaturan -> {
                    startActivity(Intent(this, PengaturanActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the home item as selected
        bottomNavigation.selectedItemId = R.id.nav_home
    }
}