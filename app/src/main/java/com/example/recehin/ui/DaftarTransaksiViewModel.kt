package com.example.recehin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recehin.data.network.request.AddBillRequest
import com.example.recehin.data.network.request.AddTransactionRequest
import com.example.recehin.data.network.response.Bill
import com.example.recehin.data.network.response.Category
import com.example.recehin.data.network.response.Transaction
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DaftarTransaksiViewModel(private val repository: TransactionRepository) : ViewModel() {

    // Daftar asli dari server, tidak diekspos ke UI
    private val _masterTransactionList = MutableLiveData<List<Transaction>>()

    // Daftar yang sudah difilter dan akan ditampilkan di UI
    private val _displayedTransactions = MutableLiveData<List<Transaction>>()
    val displayedTransactions: LiveData<List<Transaction>> = _displayedTransactions

    private val _pieChartData = MutableLiveData<Map<String, Float>>()
    val pieChartData: LiveData<Map<String, Float>> = _pieChartData

    // Variabel internal untuk menyimpan status filter
    private var currentFilterType: String? = null // null = semua, "income", "expense"
    private var currentSearchKeyword: String? = null

    // LiveData lain yang sudah ada
    private val _incomeCategories = MutableLiveData<List<Category>>()
    val incomeCategories: LiveData<List<Category>> = _incomeCategories
    private val _expenseCategories = MutableLiveData<List<Category>>()
    val expenseCategories: LiveData<List<Category>> = _expenseCategories
    private val _addTransactionSuccess = SingleLiveEvent<Boolean>()
    val addTransactionSuccess: LiveData<Boolean> = _addTransactionSuccess
    private val _bills = MutableLiveData<List<Bill>>()
    val bills: LiveData<List<Bill>> = _bills
    private val _addBillSuccess = SingleLiveEvent<Boolean>()
    val addBillSuccess: LiveData<Boolean> = _addBillSuccess
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun fetchInitialData() {
        fetchTransactions()
        fetchCategories()
        fetchBills()
    }

    fun fetchTransactions() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getAllTransactions()
                _masterTransactionList.postValue(result)
                applyFilters(result) // Terapkan filter setelah data masuk
            } catch (e: Exception) {
                _toastMessage.postValue("Gagal memuat transaksi: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setFilterType(type: String?) {
        currentFilterType = type
        applyFilters()
    }

    fun setSearchKeyword(keyword: String?) {
        currentSearchKeyword = keyword
        applyFilters()
    }
    private fun processForPieChart(transactions: List<Transaction>) {
        // Ambil bulan dan tahun saat ini dari waktu perangkat
        val deviceCalendar = Calendar.getInstance()
        val currentMonth = deviceCalendar.get(Calendar.MONTH)
        val currentYear = deviceCalendar.get(Calendar.YEAR)

        // Buat format tanggal untuk mem-parsing data dari server
        val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        val expensesThisMonth = transactions.filter {
            // 1. Pastikan ini adalah pengeluaran
            it.categoryType.equals("expense", ignoreCase = true) &&
                    try {
                        // 2. Coba parse tanggal transaksi
                        val transactionDate = serverDateFormat.parse(it.transactionDate)
                        if (transactionDate != null) {
                            // 3. Buat instance Calendar BARU untuk setiap transaksi (ini perbaikannya)
                            val transactionCalendar = Calendar.getInstance()
                            transactionCalendar.time = transactionDate
                            // 4. Bandingkan bulan dan tahunnya
                            transactionCalendar.get(Calendar.MONTH) == currentMonth &&
                                    transactionCalendar.get(Calendar.YEAR) == currentYear
                        } else {
                            false
                        }
                    } catch (e: Exception) {
                        false // Jika tanggal tidak bisa di-parse, abaikan
                    }
        }

        // Kelompokkan pengeluaran yang sudah terfilter berdasarkan nama kategori
        val categoryTotals = expensesThisMonth
            .groupBy { it.categoryName }
            .mapValues { entry ->
                entry.value.sumOf { it.amount }.toFloat()
            }

        _pieChartData.postValue(categoryTotals)
    }
    private fun applyFilters(sourceList: List<Transaction>? = null) {
        val masterList = sourceList ?: _masterTransactionList.value ?: return

        var filteredList = masterList

        // Langkah 1: Filter berdasarkan Tipe (Tab)
        currentFilterType?.let { type ->
            filteredList = filteredList.filter { it.categoryType.equals(type, ignoreCase = true) }
        }

        // Langkah 2: Filter berdasarkan Pencarian (Search)
        currentSearchKeyword?.let { keyword ->
            if (keyword.isNotBlank()) {
                filteredList = filteredList.filter { transaction ->
                    val keywordLower = keyword.lowercase(Locale.getDefault())
                    (transaction.description?.lowercase(Locale.getDefault())?.contains(keywordLower) == true) ||
                            (transaction.categoryName.lowercase(Locale.getDefault()).contains(keywordLower))
                }
            }
        }

        // Kirim hasil yang sudah difilter dan diurutkan ke UI
        _displayedTransactions.postValue(filteredList.sortedByDescending { it.transactionDate })
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                _incomeCategories.postValue(repository.getCategories("income"))
                _expenseCategories.postValue(repository.getCategories("expense"))
            } catch (e: Exception) { /* Silent fail */ }
        }
    }

    fun addTransaction(amount: Double, categoryId: Int, description: String?, transactionDate: String) {
        viewModelScope.launch {
            try {
                val request = AddTransactionRequest(amount, categoryId, description, transactionDate)
                val response = repository.addTransaction(request)
                if (response.status == "success") {
                    _toastMessage.postValue("Transaksi berhasil ditambahkan!")
                    _addTransactionSuccess.call()
                } else {
                    _toastMessage.postValue("Gagal: ${response.message}")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }

    fun fetchBills() {
        viewModelScope.launch {
            try {
                _bills.postValue(repository.getBills())
            } catch (e: Exception) {
                _toastMessage.postValue("Gagal memuat tagihan: ${e.message}")
            }
        }
    }

    fun addBill(request: AddBillRequest) {
        viewModelScope.launch {
            try {
                val response = repository.addBill(request)
                if (response.status == "success") {
                    _toastMessage.postValue("Tagihan rutin berhasil ditambahkan!")
                    _addBillSuccess.call()
                } else {
                    _toastMessage.postValue("Gagal menambah tagihan: ${response.message}")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
