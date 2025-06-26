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
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recehin.R
import com.example.recehin.data.di.Injection
import com.example.recehin.data.network.request.AddBillRequest
import com.example.recehin.data.network.response.Category
import com.example.recehin.ui.DaftarTransaksiViewModel
import com.example.recehin.ui.ViewModelFactory
import com.example.recehin.utils.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class DaftarTransaksiActivity : BaseActivity() {

    override fun shouldApplyBottomInset(): Boolean = false

    private lateinit var viewModel: DaftarTransaksiViewModel
    private lateinit var transaksiAdapter: TransaksiAdapter

    private lateinit var searchView: SearchView
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerViewTransaksi: RecyclerView
    private lateinit var fabTambahTransaksi: FloatingActionButton
    private lateinit var btnTagihanRutin: Button
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var tvEmptyState: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_transaksi)
        rootView = findViewById(R.id.root_layout)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DaftarTransaksiViewModel::class.java]

        initViews()
        setupRecyclerView()
        setupListeners()
        setupBottomNavigation()
        setupObservers()

        viewModel.fetchInitialData()
    }

    private fun initViews() {
        recyclerViewTransaksi = findViewById(R.id.recyclerview_transaksi)
        fabTambahTransaksi = findViewById(R.id.fab_tambah_transaksi)
        btnTagihanRutin = findViewById(R.id.btn_tagihan_rutin)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tvEmptyState = findViewById(R.id.tv_empty_state)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.search_transaksi)
        tabLayout = findViewById(R.id.tab_layout)
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        transaksiAdapter = TransaksiAdapter()
        recyclerViewTransaksi.layoutManager = LinearLayoutManager(this)
        recyclerViewTransaksi.adapter = transaksiAdapter
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.toastMessage.observe(this) { if (!it.isNullOrEmpty()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }

        viewModel.displayedTransactions.observe(this) { transactions ->
            if (transactions.isNullOrEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
                recyclerViewTransaksi.visibility = View.GONE
            } else {
                tvEmptyState.visibility = View.GONE
                recyclerViewTransaksi.visibility = View.VISIBLE
                transaksiAdapter.submitList(transactions)
            }
        }

        viewModel.addTransactionSuccess.observe(this) { if (it) viewModel.fetchTransactions() }
        viewModel.addBillSuccess.observe(this) { if(it) viewModel.fetchBills() }
    }

    private fun setupListeners() {
        fabTambahTransaksi.setOnClickListener { showInputTransaksiDialog() }
        btnTagihanRutin.setOnClickListener { showTagihanRutinDialog() }
        setupSearchListener()
        setupTabListener()
    }

    private fun setupSearchListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchKeyword(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchKeyword(newText)
                return true
            }
        })
    }

    private fun setupTabListener() {
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText("Semua"))
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setFilterType(null)
                    1 -> viewModel.setFilterType("income")
                    2 -> viewModel.setFilterType("expense")
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showTagihanRutinDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_tagihan_rutin, null)
        bottomSheetDialog.setContentView(view)

        val rvTagihan = view.findViewById<RecyclerView>(R.id.recyclerview_tagihan)
        val btnTambahTagihan = view.findViewById<Button>(R.id.btn_tambah_tagihan)
        val tagihanAdapter = TagihanAdapter()

        rvTagihan.layoutManager = LinearLayoutManager(this)
        rvTagihan.adapter = tagihanAdapter

        viewModel.bills.observe(this) { bills ->
            tagihanAdapter.submitList(bills)
        }

        btnTambahTagihan.setOnClickListener {
            bottomSheetDialog.dismiss()
            showTambahTagihanDialog()
        }

        bottomSheetDialog.show()
    }

    private fun showTambahTagihanDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_tagihan, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Tambah Tagihan Rutin")
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        val etNama = dialogView.findViewById<EditText>(R.id.et_nama_tagihan)
        val etJumlah = dialogView.findViewById<EditText>(R.id.et_jumlah_tagihan)
        val spinnerFrekuensi = dialogView.findViewById<Spinner>(R.id.spinner_frekuensi)
        val spinnerKategori = dialogView.findViewById<Spinner>(R.id.spinner_kategori_tagihan)
        val btnPilihTanggal = dialogView.findViewById<Button>(R.id.btn_pilih_tanggal_tagihan)
        val switchNotifikasi = dialogView.findViewById<SwitchCompat>(R.id.switch_notifikasi)

        val frekuensiOptions = arrayOf("Harian", "Mingguan", "Bulanan", "Tahunan")
        spinnerFrekuensi.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, frekuensiOptions)

        var expenseCategories: List<Category> = emptyList()
        viewModel.expenseCategories.observe(this) { categories ->
            expenseCategories = categories
            spinnerKategori.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories.map { it.name })
        }

        var selectedDate = Date()
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        btnPilihTanggal.text = apiDateFormat.format(selectedDate)
        btnPilihTanggal.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                selectedDate = calendar.time
                btnPilihTanggal.text = apiDateFormat.format(selectedDate)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_BILL")
        }

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = etNama.text.toString()
                val amountStr = etJumlah.text.toString()

                if (name.isEmpty() || amountStr.isEmpty()) {
                    Toast.makeText(this, "Nama dan Jumlah harus diisi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val selectedCategoryName = spinnerKategori.selectedItem?.toString()
                if (selectedCategoryName == null) {
                    Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val category = expenseCategories.find { it.name == selectedCategoryName }
                if (category == null) {
                    Toast.makeText(this, "Kategori tidak valid", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val request = AddBillRequest(
                    name = name,
                    amount = amountStr.toDouble(),
                    categoryId = category.id,
                    nextDueDate =  apiDateFormat.format(selectedDate),
                    frequency = spinnerFrekuensi.selectedItem.toString().lowercase(Locale.getDefault()),
                    notification = switchNotifikasi.isChecked
                )

                viewModel.addBill(request)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showInputTransaksiDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_input_transaksi, null)
        bottomSheetDialog.setContentView(view)

        val radioGroupTipe = view.findViewById<RadioGroup>(R.id.radio_group_tipe)
        val spinnerKategori = view.findViewById<Spinner>(R.id.spinner_kategori)
        val editTextJumlah = view.findViewById<EditText>(R.id.et_jumlah)
        val editTextKeterangan = view.findViewById<EditText>(R.id.et_keterangan)
        val btnPilihTanggal = view.findViewById<Button>(R.id.btn_pilih_tanggal)
        val btnSimpan = view.findViewById<Button>(R.id.btn_simpan)
        val btnBatal = view.findViewById<Button>(R.id.btn_batal)

        btnBatal.setOnClickListener { bottomSheetDialog.dismiss() }

        var selectedDate = Date()
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("dd MMMM yy", Locale("id", "ID"))
        btnPilihTanggal.text = displayDateFormat.format(selectedDate)

        btnPilihTanggal.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(selectedDate.time)
                .build()
            datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = timeInMillis
                selectedDate = calendar.time
                btnPilihTanggal.text = displayDateFormat.format(selectedDate)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        var currentCategories: List<Category> = emptyList()
        val setupSpinner: (List<Category>?) -> Unit = { categories ->
            currentCategories = categories ?: emptyList()
            val categoryNames = currentCategories.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryNames)
            spinnerKategori.adapter = adapter
        }

        radioGroupTipe.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_pemasukan) {
                setupSpinner(viewModel.incomeCategories.value)
            } else {
                setupSpinner(viewModel.expenseCategories.value)
            }
        }

        viewModel.expenseCategories.observe(this) { categories ->
            if (radioGroupTipe.checkedRadioButtonId == R.id.radio_pengeluaran) {
                setupSpinner(categories)
            }
        }
        viewModel.incomeCategories.observe(this) { categories ->
            if (radioGroupTipe.checkedRadioButtonId == R.id.radio_pemasukan) {
                setupSpinner(categories)
            }
        }
        radioGroupTipe.check(R.id.radio_pengeluaran)

        btnSimpan.setOnClickListener {
            val amountStr = editTextJumlah.text.toString()
            if (amountStr.isEmpty()) {
                editTextJumlah.error = "Jumlah tidak boleh kosong"
                return@setOnClickListener
            }
            if (spinnerKategori.selectedItem == null) {
                Toast.makeText(this, "Kategori belum dimuat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val amount = amountStr.toDouble()
            val description = editTextKeterangan.text.toString().ifEmpty { null }
            val selectedCategoryName = spinnerKategori.selectedItem.toString()
            val selectedCategory = currentCategories.find { it.name == selectedCategoryName }
            if (selectedCategory == null) {
                Toast.makeText(this, "Kategori tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addTransaction(
                amount,
                selectedCategory.id,
                description,
                apiDateFormat.format(selectedDate)
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_transaksi
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { startActivity(Intent(this, BerandaActivity::class.java)); true }
                R.id.nav_transaksi -> true
                R.id.nav_pengaturan -> { startActivity(Intent(this, PengaturanActivity::class.java)); true }
                else -> false
            }
        }
    }
}
