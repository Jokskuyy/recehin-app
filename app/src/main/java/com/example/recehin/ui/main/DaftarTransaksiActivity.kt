package com.example.recehin.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.ViewGroup
import android.content.res.Resources
import com.example.recehin.BaseActivity
import com.example.recehin.R

class DaftarTransaksiActivity : BaseActivity() {

    override fun shouldApplyBottomInset(): Boolean = false

    private lateinit var searchView: SearchView
    private lateinit var chipGroupFilter: ChipGroup
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerViewTransaksi: RecyclerView
    private lateinit var btnExport: Button
    private lateinit var fabTambahTransaksi: FloatingActionButton
    private lateinit var btnTagihanRutin: Button
    private lateinit var containerAnalisis: CardView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_transaksi)

        rootView = findViewById(R.id.root_layout)

        // Initialize views
        initViews()

        fabTambahTransaksi.post {
            val root = findViewById<View>(android.R.id.content)
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                val layoutParams = fabTambahTransaksi.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = systemBars.bottom + 66.dp // 16dp = your base margin
                fabTambahTransaksi.layoutParams = layoutParams

                insets
            }

            ViewCompat.requestApplyInsets(root)
        }

        // Setup UI interactions
        setupListeners()

        // Setup bottom navigation
        setupBottomNavigation()

        // Load dummy data
        loadDummyData()
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun initViews() {
        // Find all views
        searchView = findViewById(R.id.search_transaksi)
        chipGroupFilter = findViewById(R.id.chipgroup_filter)
        tabLayout = findViewById(R.id.tab_layout)
        recyclerViewTransaksi = findViewById(R.id.recyclerview_transaksi)
        btnExport = findViewById(R.id.btn_export)
        fabTambahTransaksi = findViewById(R.id.fab_tambah_transaksi)
        btnTagihanRutin = findViewById(R.id.btn_tagihan_rutin)
        containerAnalisis = findViewById(R.id.card_analisis)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        // Setup RecyclerView (dummy adapter would be implemented in real app)
        recyclerViewTransaksi.layoutManager = LinearLayoutManager(this)
        // In reality you would implement an adapter

        // Setup TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Semua"))
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))
    }

    private fun setupListeners() {
        // Back button click
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Filter chip clicks
        setupFilterChips()

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Would filter transactions in a real app
                Toast.makeText(this@DaftarTransaksiActivity, "Mencari: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Real-time filtering would happen here
                return true
            }
        })

        // Export button
        btnExport.setOnClickListener {
            showExportDialog()
        }

        // FAB for adding transaction
        fabTambahTransaksi.setOnClickListener {
            showInputTransaksiDialog()
        }

        // Routine bills button
        btnTagihanRutin.setOnClickListener {
            showTagihanRutinDialog()
        }

        // Analysis section
        containerAnalisis.setOnClickListener {
            // In a real app, this might navigate to a detailed analysis screen
            Toast.makeText(this, "Membuka analisis keuangan detail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupFilterChips() {
        // Add filter options
        val filterOptions = listOf("Hari ini", "Minggu ini", "Bulan ini", "Kustom")

        for (option in filterOptions) {
            val chip = Chip(this).apply {
                text = option
                isCheckable = true
                isClickable = true
            }

            chip.setOnClickListener {
                if (option == "Kustom") {
                    showDateRangePickerDialog()
                } else {
                    Toast.makeText(this@DaftarTransaksiActivity, "Filter: $option", Toast.LENGTH_SHORT).show()
                }
            }

            chipGroupFilter.addView(chip)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    true
                }
                R.id.nav_transaksi -> {
                    // Already on this screen
                    true
                }
//                R.id.nav_analisis -> {
//                    // Navigate to analysis (in a real app)
//                    Toast.makeText(this, "Navigasi ke Analisis", Toast.LENGTH_SHORT).show()
//                    true
//                }
                R.id.nav_pengaturan -> {
                    startActivity(Intent(this, PengaturanActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the transactions tab as selected
        bottomNavigation.selectedItemId = R.id.nav_transaksi
    }

    private fun loadDummyData() {
        val dummyList = listOf("Gaji", "Makan", "Transportasi", "Investasi", "Hiburan")
        val adapter = TransaksiAdapter(dummyList)
        recyclerViewTransaksi.adapter = adapter
    }

    private fun showDateRangePickerDialog() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pilih Rentang Tanggal")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = Date(selection.first)
            val endDate = Date(selection.second)
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id"))

            Toast.makeText(
                this,
                "Filter: ${dateFormat.format(startDate)} - ${dateFormat.format(endDate)}",
                Toast.LENGTH_SHORT
            ).show()
        }

        dateRangePicker.show(supportFragmentManager, "DATE_RANGE_PICKER")
    }

    private fun showExportDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ekspor Data Transaksi")
            .setItems(arrayOf("Ekspor ke PDF", "Ekspor ke Excel")) { _, which ->
                val format = if (which == 0) "PDF" else "Excel"
                Toast.makeText(this, "Mengekspor transaksi ke $format", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showInputTransaksiDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_input_transaksi, null)

        // Setup view elements
        val radioGroupTipe = view.findViewById<RadioGroup>(R.id.radio_group_tipe)
        val spinnerKategori = view.findViewById<Spinner>(R.id.spinner_kategori)
        val editTextJumlah = view.findViewById<EditText>(R.id.et_jumlah)
        val editTextKeterangan = view.findViewById<EditText>(R.id.et_keterangan)
        val btnPilihTanggal = view.findViewById<Button>(R.id.btn_pilih_tanggal)
        val btnSimpan = view.findViewById<Button>(R.id.btn_simpan)
        val btnBatal = view.findViewById<Button>(R.id.btn_batal)

        // Setup date picker
        var selectedDateInMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
        btnPilihTanggal.text = dateFormat.format(Date(selectedDateInMillis))

        btnPilihTanggal.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Transaksi")
                .setSelection(selectedDateInMillis)
                .build()

            datePicker.addOnPositiveButtonClickListener { dateInMillis ->
                selectedDateInMillis = dateInMillis
                btnPilihTanggal.text = dateFormat.format(Date(dateInMillis))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Setup category spinner based on transaction type
        radioGroupTipe.setOnCheckedChangeListener { _, checkedId ->
            val categories = if (checkedId == R.id.radio_pemasukan) {
                arrayOf("Gaji", "Bonus", "THR", "Hadiah", "Investasi", "Lainnya")
            } else {
                arrayOf("Kebutuhan Pokok", "Transportasi", "Hiburan", "Kesehatan", "Pendidikan", "Lainnya")
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
            spinnerKategori.adapter = adapter
        }

        // Set default to "Pengeluaran" categories
        radioGroupTipe.check(R.id.radio_pengeluaran)

        // Handle save button
        btnSimpan.setOnClickListener {
            val tipe = if (radioGroupTipe.checkedRadioButtonId == R.id.radio_pemasukan)
                "Pemasukan" else "Pengeluaran"
            val kategori = spinnerKategori.selectedItem.toString()
            val jumlah = editTextJumlah.text.toString()
            val keterangan = editTextKeterangan.text.toString()
            val tanggal = btnPilihTanggal.text.toString()

            if (jumlah.isEmpty()) {
                editTextJumlah.error = "Jumlah harus diisi"
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Transaksi $tipe: Rp$jumlah untuk $kategori pada $tanggal berhasil disimpan",
                Toast.LENGTH_LONG
            ).show()

            bottomSheetDialog.dismiss()

            // In a real app, you would save this data to a database
            // and refresh the transactions list
        }

        btnBatal.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun showTagihanRutinDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_tagihan_rutin, null)

        // Setup UI elements
        val recyclerViewTagihan = view.findViewById<RecyclerView>(R.id.recyclerview_tagihan)
        val btnTambahTagihan = view.findViewById<Button>(R.id.btn_tambah_tagihan)

        // Setup RecyclerView (dummy implementation)
        recyclerViewTagihan.layoutManager = LinearLayoutManager(this)
        // Would use a real adapter in production

        // Add tagihan button
        btnTambahTagihan.setOnClickListener {
            bottomSheetDialog.dismiss()
            showTambahTagihanDialog()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun showTambahTagihanDialog() {
        val dialog = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_tagihan, null)

        // Setup UI elements
        val editTextNama = view.findViewById<EditText>(R.id.et_nama_tagihan)
        val editTextJumlah = view.findViewById<EditText>(R.id.et_jumlah_tagihan)
        val spinnerFrekuensi = view.findViewById<Spinner>(R.id.spinner_frekuensi)
        val spinnerKategori = view.findViewById<Spinner>(R.id.spinner_kategori_tagihan)
        val btnPilihTanggal = view.findViewById<Button>(R.id.btn_pilih_tanggal_tagihan)
        val switchNotifikasi = view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch_notifikasi)

        // Setup date selection
        var selectedDateInMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
        btnPilihTanggal.text = dateFormat.format(Date(selectedDateInMillis))

        btnPilihTanggal.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Jatuh Tempo")
                .setSelection(selectedDateInMillis)
                .build()

            datePicker.addOnPositiveButtonClickListener { dateInMillis ->
                selectedDateInMillis = dateInMillis
                btnPilihTanggal.text = dateFormat.format(Date(dateInMillis))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Setup spinners
        val frekuensiAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Harian", "Mingguan", "Bulanan", "Tahunan")
        )
        spinnerFrekuensi.adapter = frekuensiAdapter

        val kategoriAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Kebutuhan Pokok", "Transportasi", "Hiburan", "Kesehatan", "Pendidikan", "Lainnya")
        )
        spinnerKategori.adapter = kategoriAdapter

        dialog.setView(view)
            .setTitle("Tambah Tagihan Rutin")
            .setPositiveButton("Simpan") { _, _ ->
                val nama = editTextNama.text.toString()
                val jumlah = editTextJumlah.text.toString()
                val frekuensi = spinnerFrekuensi.selectedItem.toString()
                val kategori = spinnerKategori.selectedItem.toString()
                val tanggal = btnPilihTanggal.text.toString()
                val notifikasi = if (switchNotifikasi.isChecked) "aktif" else "tidak aktif"

                Toast.makeText(
                    this,
                    "Tagihan $nama sebesar Rp$jumlah ($frekuensi) berhasil disimpan dengan notifikasi $notifikasi",
                    Toast.LENGTH_LONG
                ).show()

                // In a real app, you would save to a database
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}