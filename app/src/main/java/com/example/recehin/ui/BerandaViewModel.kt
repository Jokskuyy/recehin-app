package com.example.recehin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recehin.data.network.response.Summary
import com.example.recehin.data.network.response.Transaction
import com.example.recehin.data.network.response.UserProfile
import com.example.recehin.data.repository.TransactionRepository
import com.example.recehin.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TreeMap

class BerandaViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _summary = MutableLiveData<Summary>()
    val summary: LiveData<Summary> = _summary

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _latestTransactions = MutableLiveData<List<Transaction>>()
    val latestTransactions: LiveData<List<Transaction>> = _latestTransactions

    // --- LIVE DATA BARU UNTUK GRAFIK ---
    private val _chartData = MutableLiveData<List<Pair<String, Float>>>()
    val chartData: LiveData<List<Pair<String, Float>>> = _chartData
    // ------------------------------------

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadDashboardData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Pengambilan data tidak berubah
                val profileResult = repository.getProfile()
                val transactionsResult = repository.getAllTransactions()

                // Memproses data untuk UI
                processTransactionsForUI(transactionsResult)
                _userProfile.postValue(profileResult)

            } catch (e: Exception) {
                _errorMessage.postValue("Gagal memuat data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun processTransactionsForUI(transactions: List<Transaction>) {
        // --- 1. Proses untuk Ringkasan Saldo dan Transaksi Terbaru (Kode Lama) ---
        var totalIncome = 0.0
        var totalExpense = 0.0
        transactions.forEach {
            if (it.categoryType.equals("income", ignoreCase = true)) totalIncome += it.amount
            else totalExpense += it.amount
        }
        val sortedTransactions = transactions.sortedByDescending { it.transactionDate }
        _summary.postValue(Summary(totalIncome, totalExpense))
        _latestTransactions.postValue(sortedTransactions.take(3))

        // --- 2. Proses BARU untuk Data Grafik ---
        processExpensesForChart(transactions)
    }

    private fun processExpensesForChart(transactions: List<Transaction>) {
        // Menggunakan TreeMap agar hari-hari otomatis terurut
        val dailyExpenses = TreeMap<String, Float>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Inisialisasi 7 hari terakhir dengan nilai 0
        for (i in 0..6) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateKey = dateFormat.format(calendar.time)
            dailyExpenses[dateKey] = 0f
        }

        // Filter hanya pengeluaran dalam 7 hari terakhir
        val sevenDaysAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }.time
        val recentExpenses = transactions.filter {
            val transactionDate = try {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(it.transactionDate)
            } catch (e: Exception) { null }

            it.categoryType.equals("expense", ignoreCase = true) && transactionDate != null && !transactionDate.before(sevenDaysAgo)
        }

        // Akumulasi total pengeluaran per hari
        for (expense in recentExpenses) {
            val transactionDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(expense.transactionDate)
            val dateKey = dateFormat.format(transactionDate!!)
            dailyExpenses[dateKey] = (dailyExpenses[dateKey] ?: 0f) + expense.amount.toFloat()
        }

        // Ubah data map menjadi list of pairs (Label Hari, Jumlah)
        val dayFormat = SimpleDateFormat("E", Locale("id", "ID")) // "E" untuk nama hari singkat (Sen, Sel, Rab)
        val chartEntries = dailyExpenses.map { (dateStr, total) ->
            val date = dateFormat.parse(dateStr)
            val dayLabel = dayFormat.format(date!!)
            Pair(dayLabel, total)
        }

        _chartData.postValue(chartEntries)
    }
}
