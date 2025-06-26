// File: app/src/main/kotlin/com/example/recehin/data/network/response/TransactionResponse.kt
package com.example.recehin.data.network.response

import com.google.gson.annotations.SerializedName

// Wrapper untuk response list transaksi
data class TransactionListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Transaction>
)

// Wrapper untuk response ringkasan keuangan (dashboard)
data class SummaryResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Summary
)

// Model untuk satu item transaksi
// Didesain agar backend bisa mengirimkan nama kategori sekaligus
data class Transaction(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("description")
    val description: String?,

    @SerializedName("transaction_date")
    val transactionDate: String, // Format: "YYYY-MM-DD"

    // Asumsi backend mengirimkan data ini setelah JOIN dengan tabel categories
    @SerializedName("category_name")
    val categoryName: String,

    @SerializedName("category_type")
    val categoryType: String // "income" or "expense"
)

// Model untuk ringkasan keuangan
data class Summary(
    @SerializedName("total_income")
    val totalIncome: Double,

    @SerializedName("total_expense")
    val totalExpense: Double
)