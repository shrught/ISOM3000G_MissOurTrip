package com.example.missourtrip

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.expense_layout.view.*

class ExpenseAdapter(private var expenses: List<Expense>): RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder>() {

    class ExpenseHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.name)
        val amount: TextView = view.findViewById(R.id.amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_layout,parent,false)
        return ExpenseHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        val expense = expenses[position]

        holder.name.text = expense.name
        holder.amount.text = "$%.2f".format(expense.amount)

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    fun setData(expenses: List<Expense>){
        this.expenses = expenses
        notifyDataSetChanged()
    }
}