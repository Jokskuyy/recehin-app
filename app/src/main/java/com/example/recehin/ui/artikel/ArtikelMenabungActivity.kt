package com.example.recehin.ui.artikel

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recehin.R

class ArtikelMenabungActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel_menabung)

        setupToolbar()
        setupViews()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_artikel)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Sembunyikan judul default
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViews() {
        val tvTitle = findViewById<TextView>(R.id.tv_artikel_title)
        val tvContent = findViewById<TextView>(R.id.tv_artikel_content)
        val ivArtikel = findViewById<ImageView>(R.id.iv_artikel)

        tvTitle.text = "Tips Menabung Efektif untuk Masa Depan"

        val content = """
            Menabung merupakan salah satu kebiasaan finansial yang paling penting untuk mempersiapkan masa depan yang lebih baik. Namun, tidak semua orang tahu cara menabung yang efektif dan berkelanjutan.

            Mengapa Menabung Penting?

            Menabung bukan hanya tentang menyisihkan uang, tetapi juga tentang membangun keamanan finansial dan mencapai tujuan hidup Anda. Dengan menabung yang tepat, Anda dapat:

            • Memiliki dana darurat untuk situasi tak terduga
            • Mewujudkan impian seperti membeli rumah atau kendaraan
            • Mempersiapkan dana pensiun
            • Mengurangi stres finansial
            • Memiliki kebebasan finansial di masa depan

            Strategi Menabung yang Efektif

            1. Tentukan Tujuan yang Jelas
            Sebelum mulai menabung, tetapkan tujuan yang spesifik, terukur, dan memiliki batas waktu. Contoh:
            • Dana darurat sebesar 6 bulan pengeluaran dalam 1 tahun
            • DP rumah Rp 100 juta dalam 3 tahun
            • Dana liburan Rp 10 juta dalam 6 bulan

            2. Gunakan Aturan 50/30/20
            Alokasikan penghasilan Anda dengan proporsi:
            • 50% untuk kebutuhan pokok (makanan, tempat tinggal, transportasi)
            • 30% untuk keinginan (hiburan, makan di luar, hobi)
            • 20% untuk tabungan dan investasi

            3. Otomatisasi Tabungan
            Buat transfer otomatis dari rekening utama ke rekening tabungan setiap tanggal gajian. Ini memastikan Anda menabung sebelum uang terpakai untuk hal lain.

            4. Mulai dari Nominal Kecil
            Jangan merasa harus menabung jumlah besar dari awal. Mulai dengan Rp 50.000 per minggu, lalu tingkatkan secara bertahap.

            5. Pisahkan Rekening Tabungan
            Buat rekening khusus untuk tabungan yang terpisah dari rekening harian. Ini mengurangi godaan untuk menggunakannya.

            Tips Tambahan untuk Sukses Menabung

            • Catat semua pengeluaran untuk mengetahui ke mana uang Anda pergi
            • Kurangi pengeluaran yang tidak perlu seperti langganan yang jarang digunakan
            • Manfaatkan aplikasi keuangan untuk memantau progress tabungan
            • Rayakan pencapaian kecil untuk mempertahankan motivasi
            • Jangan menyentuh tabungan kecuali untuk tujuan yang sudah ditetapkan

            Kesimpulan

            Menabung efektif membutuhkan disiplin dan komitmen, tetapi manfaatnya akan terasa di masa depan. Mulai dari sekarang, tetapkan tujuan yang jelas, buat rencana yang realistis, dan konsisten dalam menjalankannya. Ingat, tidak ada kata terlambat untuk memulai kebiasaan menabung yang baik.

            Dengan menerapkan tips-tips di atas, Anda akan dapat membangun keamanan finansial dan mencapai impian-impian Anda. Selamat menabung!
        """.trimIndent()

        tvContent.text = content
        ivArtikel.setImageResource(R.drawable.img_article1)
    }
}
