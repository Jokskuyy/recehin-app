package com.example.recehin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransaksiAdapter(private val transaksiList: List<String>) :
    RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    class TransaksiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKategori: TextView = view.findViewById(R.id.tv_category)
        val tvJumlah: TextView = view.findViewById(R.id.tv_amount)
        val tvDeskripsi: TextView = view.findViewById(R.id.tv_description)
        val tvTanggal: TextView = view.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val dummyText = transaksiList[position]
        holder.tvKategori.text = "$dummyText"
        holder.tvDeskripsi.text = "deskripsi"
        holder.tvJumlah.text = "Rp 100.000"
        holder.tvTanggal.text = "01 Januari 2024"
    }

    override fun getItemCount(): Int = transaksiList.size
}
