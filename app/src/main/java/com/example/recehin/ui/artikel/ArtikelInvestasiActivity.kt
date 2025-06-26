package com.example.recehin.ui.artikel

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recehin.R

class ArtikelInvestasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel_investasi)

        setupToolbar()
        setupViews()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_artikel)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViews() {
        val tvTitle = findViewById<TextView>(R.id.tv_artikel_title)
        val tvContent = findViewById<TextView>(R.id.tv_artikel_content)
        val ivArtikel = findViewById<ImageView>(R.id.iv_artikel)

        tvTitle.text = "Investasi untuk Pemula: Panduan Lengkap"

        val content = """
            Investasi adalah kunci untuk membangun kekayaan jangka panjang dan mencapai kebebasan finansial. Bagi pemula, dunia investasi mungkin terasa rumit dan menakutkan, tetapi dengan panduan yang tepat, siapa pun bisa memulai perjalanan investasi mereka.

            Apa itu Investasi?

            Investasi adalah kegiatan menanamkan modal atau uang dengan harapan mendapatkan keuntungan di masa depan. Berbeda dengan menabung yang hanya menyimpan uang, investasi membuat uang Anda bekerja untuk menghasilkan lebih banyak uang.

            Mengapa Perlu Berinvestasi?

            • Mengalahkan inflasi yang menggerus nilai uang
            • Membangun kekayaan jangka panjang
            • Mencapai tujuan finansial lebih cepat
            • Mempersiapkan dana pensiun
            • Memiliki passive income (penghasilan pasif)

            Jenis-Jenis Investasi untuk Pemula

            1. Deposito
            • Risiko rendah dengan return yang pasti
            • Cocok untuk dana darurat
            • Return sekitar 4-6% per tahun
            • Dijamin oleh LPS hingga Rp 2 miliar

            2. Reksa Dana
            • Dikelola oleh manajer investasi profesional
            • Diversifikasi otomatis
            • Mulai dari Rp 100.000
            • Jenis: pasar uang, pendapatan tetap, campuran, saham

            3. Saham
            • Potensi return tinggi
            • Risiko tinggi
            • Perlu pemahaman analisis
            • Mulai dari Rp 100.000

            4. Obligasi
            • Return lebih stabil dari saham
            • Risiko sedang
            • Cocok untuk diversifikasi portofolio

            5. Emas
            • Hedge against inflation
            • Mudah dicairkan
            • Bisa fisik atau digital

            Langkah Memulai Investasi

            1. Tentukan Tujuan Investasi
            • Jangka pendek (1-3 tahun): deposito, reksa dana pasar uang
            • Jangka menengah (3-5 tahun): reksa dana campuran
            • Jangka panjang (>5 tahun): reksa dana saham, saham

            2. Kenali Profil Risiko Anda
            • Konservatif: deposito, reksa dana pasar uang
            • Moderat: reksa dana campuran, obligasi
            • Agresif: saham, reksa dana saham

            3. Siapkan Dana Darurat Dulu
            Pastikan memiliki dana darurat 6-12 bulan pengeluaran sebelum berinvestasi.

            4. Mulai dengan Investasi Sederhana
            Reksa dana adalah pilihan terbaik untuk pemula karena:
            • Dikelola profesional
            • Diversifikasi otomatis
            • Modal kecil
            • Likuiditas tinggi

            5. Konsisten dan Disiplin
            Lakukan investasi rutin (Dollar Cost Averaging) untuk mengurangi risiko volatilitas.

            Prinsip Investasi yang Harus Diingat

            • High Risk, High Return - semakin tinggi risiko, semakin tinggi potensi keuntungan
            • Don't Put All Eggs in One Basket - diversifikasi investasi
            • Time in Market beats Timing the Market - konsistensi lebih penting dari timing
            • Compound Interest is the 8th Wonder - kekuatan bunga berbunga

            Kesalahan Umum Pemula

            1. Tidak memiliki tujuan yang jelas
            2. Tergiur return tinggi tanpa mempertimbangkan risiko
            3. Panic selling saat market turun
            4. Tidak diversifikasi
            5. Menunda-nunda memulai investasi

            Platform Investasi Terpercaya di Indonesia

            • Reksa Dana: Bibit, Bareksa, Tanamduit
            • Saham: Stockbit, Ajaib, Mandiri Sekuritas
            • P2P Lending: Modalku, Amartha, Akseleran
            • Robo Advisor: Bibit, Ajaib

            Tips Sukses Berinvestasi

            • Pelajari terus tentang investasi
            • Mulai sekarang juga, jangan menunda
            • Investasi rutin setiap bulan
            • Review portofolio secara berkala
            • Jangan emosional dalam berinvestasi
            • Konsultasi dengan financial advisor jika perlu

            Kesimpulan

            Investasi adalah marathon, bukan sprint. Mulai dengan langkah kecil, konsisten, dan terus belajar. Semakin cepat Anda memulai, semakin besar keuntungan yang bisa diperoleh dari compound interest. Ingat, investasi terbaik adalah investasi pada pengetahuan dan dimulai dari sekarang.

            Jangan biarkan ketakutan menghalangi Anda untuk memulai. Dengan persiapan yang tepat dan strategi yang jelas, siapa pun bisa menjadi investor sukses. Selamat berinvestasi!
        """.trimIndent()

        tvContent.text = content
        ivArtikel.setImageResource(R.drawable.img_article_2)
    }
}
