package com.example.recehin.ui.main

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recehin.R
import com.example.recehin.data.di.Injection
import com.example.recehin.data.local.SessionManager
import com.example.recehin.ui.BerandaViewModel
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.ui.auth.HalamanLoginActivity
import com.example.recehin.utils.BaseActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.util.Locale
import com.example.recehin.ui.artikel.ArtikelMenabungActivity
import com.example.recehin.ui.artikel.ArtikelInvestasiActivity

class BerandaActivity : BaseActivity() {

    override fun shouldApplyBottomInset(): Boolean = false

    private lateinit var viewModel: BerandaViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var transaksiAdapter: TransaksiAdapter
    private lateinit var cvArtikel1: CardView
    private lateinit var cvArtikel2: CardView
    private lateinit var tvWelcome: TextView
    private lateinit var tvSaldoTotal: TextView
    private lateinit var tvPemasukan: TextView
    private lateinit var tvPengeluaran: TextView
    private lateinit var cvLihatTransaksi: CardView
    private lateinit var fabTambahTransaksi: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var ibProfil: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var rvTransaksiTerbaru: RecyclerView
    private lateinit var tvEmptyTransaksiTerbaru: TextView
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan Factory yang sudah di-upgrade
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[BerandaViewModel::class.java]
        sessionManager = SessionManager(this)

        setContentView(R.layout.activity_beranda)
        rootView = findViewById(R.id.root_layout)

        initViews()
        setupBarChartStyle()
        setupLatestTransactionsRecyclerView()
        setupListeners()
        setupBottomNavigation()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        // Memuat data setiap kali halaman kembali aktif
        viewModel.loadDashboardData()
        setupFabInsets()
    }

    private fun initViews() {
        tvWelcome = findViewById(R.id.tv_welcome)
        tvSaldoTotal = findViewById(R.id.tv_saldo_total)
        tvPemasukan = findViewById(R.id.tv_pemasukan)
        tvPengeluaran = findViewById(R.id.tv_pengeluaran)
        cvLihatTransaksi = findViewById(R.id.cv_lihat_transaksi)
        fabTambahTransaksi = findViewById(R.id.fab_tambah_transaksi)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        ibProfil = findViewById(R.id.ib_profil)
        progressBar = findViewById(R.id.progressBar)
        rvTransaksiTerbaru = findViewById(R.id.rv_transaksi_terbaru)
        tvEmptyTransaksiTerbaru = findViewById(R.id.tv_empty_transaksi_terbaru)
        barChart = findViewById(R.id.bar_chart)
        cvArtikel1 = findViewById(R.id.cv_artikel_1)
        cvArtikel2 = findViewById(R.id.cv_artikel_2)
    }

    private fun setupBarChartStyle() {
        barChart.description.isEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.setDrawBarShadow(false)
        barChart.setTouchEnabled(false)
        barChart.legend.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.DKGRAY
        xAxis.granularity = 1f

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f
        leftAxis.textColor = Color.GRAY

        barChart.axisRight.isEnabled = false
    }

    private fun updateBarChart(data: List<Pair<String, Float>>) {
        if (data.isEmpty() || data.all { it.second == 0f }) {
            barChart.visibility = View.GONE
            return
        }
        barChart.visibility = View.VISIBLE

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        data.forEachIndexed { index, pair ->
            entries.add(BarEntry(index.toFloat(), pair.second))
            labels.add(pair.first)
        }

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val dataSet = BarDataSet(entries, "Pengeluaran Harian")
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.setDrawValues(false)

        val barData = BarData(dataSet)
        barData.barWidth = 0.5f

        barChart.data = barData
        barChart.animateY(1000)
        barChart.invalidate()
    }

    private fun setupLatestTransactionsRecyclerView() {
        transaksiAdapter = TransaksiAdapter()
        rvTransaksiTerbaru.apply {
            layoutManager = LinearLayoutManager(this@BerandaActivity)
            adapter = transaksiAdapter
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            if (it.contains("401", ignoreCase = true) || it.contains("token", ignoreCase = true)) {
                sessionManager.clearAuthToken()
                val intent = Intent(this, HalamanLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        viewModel.userProfile.observe(this) { userProfile ->
            tvWelcome.text = "Selamat Datang, ${userProfile.name}!"
        }
        viewModel.summary.observe(this) { summary ->
            val totalSaldo = summary.totalIncome - summary.totalExpense
            tvPemasukan.text = summary.totalIncome.toRupiahFormat()
            tvPengeluaran.text = summary.totalExpense.toRupiahFormat()
            tvSaldoTotal.text = totalSaldo.toRupiahFormat()
        }
        viewModel.latestTransactions.observe(this) { transactions ->
            if (transactions.isNullOrEmpty()) {
                rvTransaksiTerbaru.visibility = View.GONE
                tvEmptyTransaksiTerbaru.visibility = View.VISIBLE
            } else {
                rvTransaksiTerbaru.visibility = View.VISIBLE
                tvEmptyTransaksiTerbaru.visibility = View.GONE
                transaksiAdapter.submitList(transactions)
            }
        }
        viewModel.chartData.observe(this) { chartData ->
            updateBarChart(chartData)
        }
    }

    private fun setupListeners() {
        cvLihatTransaksi.setOnClickListener {
            startActivity(Intent(this, DaftarTransaksiActivity::class.java))
        }
        fabTambahTransaksi.setOnClickListener {
            startActivity(Intent(this, DaftarTransaksiActivity::class.java))
        }
        ibProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
        cvArtikel1.setOnClickListener {
            startActivity(Intent(this, ArtikelMenabungActivity::class.java))
        }

        cvArtikel2.setOnClickListener {
            startActivity(Intent(this, ArtikelInvestasiActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
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

    private fun setupFabInsets() {
        fabTambahTransaksi.post {
            val root = findViewById<View>(android.R.id.content)
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val layoutParams = fabTambahTransaksi.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = systemBars.bottom + 66.dpToPx()
                fabTambahTransaksi.layoutParams = layoutParams
                insets
            }
            ViewCompat.requestApplyInsets(root)
        }
    }

    private fun Double.toRupiahFormat(): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(this)
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}
