package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecurrentExpenseAdapter(
    private val expenses: MutableList<RecurrentExpense>,
    private val onDelete: (RecurrentExpense) -> Unit
) : RecyclerView.Adapter<RecurrentExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.expense_name)
        val valueTextView: TextView = view.findViewById(R.id.expense_value)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurrent_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.nameTextView.text = expense.name
        holder.valueTextView.text = expense.value.toString()
        holder.deleteButton.setOnClickListener {
            onDelete(expense)
            expenses.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, expenses.size)
        }
    }

    override fun getItemCount() = expenses.size
}