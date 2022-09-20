package com.mysite.foodrunner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mysite.foodrunner.R
import com.mysite.foodrunner.model.OrderHistory

class HistoryAdapter(val context: Context, private val orderedItems: ArrayList<OrderHistory>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_fragment_row, parent, false)
        return HistoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = orderedItems[position]
        holder.restaurantName.text = item.restaurantName
        holder.restaurantBill.text = "\u20B9${item.totalCost}"
        holder.orderRecipe.text = item.foodItem.toString()
        holder.orderDate.text = item.orderDate
    }

    override fun getItemCount(): Int = orderedItems.size

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.histRestName)
        val restaurantBill: TextView = view.findViewById(R.id.histBill)
        val orderRecipe: TextView = view.findViewById(R.id.histItems)
        val orderDate: TextView = view.findViewById(R.id.histDate)
    }
}