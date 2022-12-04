package com.example.missourtrip

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList

class EditExpenseActivity : AppCompatActivity() {
    private lateinit var expense: Expense
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var categories: ArrayList<Category>
    private lateinit var catList: List<String>
    private lateinit var curencyList: List<String>
    val formatter = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_add)

        expense = intent.getSerializableExtra("transaction") as Expense

        println(expense)


    }
}