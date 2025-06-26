// File: app/src/main/kotlin/com/example/recehin/ui/TransaksiAdapter.kt
package com.example.recehin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recehin.R
import com.example.recehin.data.network.response.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransaksiAdapter : ListAdapter<Transaction, TransaksiAdapter.TransaksiViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }

    class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Semua ID ini sekarang sudah pasti ada di item_transaksi.xml
        private val tvKategori: TextView = itemView.findViewById(R.id.tv_category)
        private val tvJumlah: TextView = itemView.findViewById(R.id.tv_amount)
        private val tvDeskripsi: TextView = itemView.findViewById(R.id.tv_description)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tv_date)
        private val ivIkon: ImageView = itemView.findViewById(R.id.iv_category_icon)

        fun bind(transaction: Transaction) {
            tvKategori.text = transaction.categoryName
            tvDeskripsi.text = if (transaction.description.isNullOrEmpty()) transaction.categoryName else transaction.description
            tvTanggal.text = formatDate(transaction.transactionDate)

            val context = itemView.context
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID).apply {
                maximumFractionDigits = 0
            }

            if (transaction.categoryType.equals("income", ignoreCase = true)) {
                tvJumlah.text = "+ ${numberFormat.format(transaction.amount)}"
                tvJumlah.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                ivIkon.setBackgroundResource(R.drawable.bg_circle_secondary) // Ganti warna background ikon
                // Logika untuk ikon bisa diperluas di sini
                ivIkon.setImageResource(R.drawable.ic_salary)
            } else { // expense
                tvJumlah.text = "- ${numberFormat.format(transaction.amount)}"
                tvJumlah.setTextColor(ContextCompat.getColor(context, R.color.colorError))
                ivIkon.setBackgroundResource(R.drawable.bg_circle_primary) // Ganti warna background ikon
                // Logika untuk ikon bisa diperluas di sini
                ivIkon.setImageResource(getExpenseIcon(transaction.categoryName))
            }
        }

        // Helper function untuk memilih ikon pengeluaran berdasarkan nama kategori
        private fun getExpenseIcon(categoryName: String): Int {
            return when (categoryName.lowercase(Locale.getDefault())) {
                "makanan", "sembako" -> R.drawable.ic_food
                "transportasi" -> R.drawable.ic_transaction // Ganti dengan ikon transportasi jika ada
                "hiburan" -> R.drawable.ic_transaction// Ganti dengan ikon hiburan jika ada
                "tagihan" -> R.drawable.ic_pengaturan // Ganti dengan ikon tagihan jika ada
                else -> R.drawable.ic_transaction // Ikon default
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                // Format input dari server: "2025-06-11T17:00:00.000Z"
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                // Format output yang diinginkan: "11 Juni 2025"
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                dateString // Kembalikan tanggal asli jika format gagal
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }
        }
    }
}