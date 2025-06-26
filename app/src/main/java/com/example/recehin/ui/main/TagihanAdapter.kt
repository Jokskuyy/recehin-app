package com.example.recehin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recehin.R
import com.example.recehin.data.network.response.Bill
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TagihanAdapter : ListAdapter<Bill, TagihanAdapter.TagihanViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagihanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tagihan, parent, false)
        return TagihanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagihanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TagihanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ID ini sekarang PASTI ada di item_tagihan.xml
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_bill_icon)
        private val tvBillName: TextView = itemView.findViewById(R.id.tv_bill_name)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tv_due_date)
        private val tvBillAmount: TextView = itemView.findViewById(R.id.tv_bill_amount)

        fun bind(bill: Bill) {
            tvBillName.text = bill.name
            tvDueDate.text = "Jatuh tempo: ${formatDate(bill.dueDate)}"

            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID).apply {
                maximumFractionDigits = 0
            }
            tvBillAmount.text = numberFormat.format(bill.amount)
        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = if (dateString.contains("T")) {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                } else {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                }
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                dateString
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Bill>() {
            override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
                return oldItem == newItem
            }
        }
    }
}
