package com.example.missourtrip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.android.synthetic.main.activity_category_main.*
import kotlinx.android.synthetic.main.activity_expense_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.StringReader
import kotlin.math.exp


class ExpenseActivity : AppCompatActivity() {
    private lateinit var deletedExpense: Expense
    private lateinit var expenses: List<Expense>
    private lateinit var oldExpenses: List<Expense>
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager_exp: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var dbExport: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_main)

        expenses = arrayListOf()

        expenseAdapter = ExpenseAdapter(expenses)
        linearLayoutManager_exp = LinearLayoutManager(this)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").fallbackToDestructiveMigration().build()
        recycleView_exp.apply {
            adapter = expenseAdapter
            layoutManager = linearLayoutManager_exp
        }
        recycleView_exp.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //swipe to remove
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteExpense(expenses[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recycleView_exp)


        add_btn.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        export_btn.setOnClickListener {
            exportCSV()
        }

    }

    private fun exportCSV(){
        val path = filesDir
        val exportCsv = File(path, "expenses.csv")
        if(!exportCsv.exists()){
            exportCsv.createNewFile()
        }

        dbExport = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").allowMainThreadQueries().build()


        var simpleExpenseList = arrayListOf<List<String>>()
        for(expense in dbExport.expenseDao().getAll()){
            simpleExpenseList.add(listOf(expense.name, expense.amount.toString(), expense.category) )
        }
        csvWriter().writeAll(simpleExpenseList, exportCsv)
        Toast.makeText(this,"Export Successful", Toast.LENGTH_SHORT).show()

    }

    private fun fetchAllExp() {
        GlobalScope.launch {
            expenses = db.expenseDao().getAll()

            runOnUiThread {
                expenseAdapter.setData(expenses)
            }
        }
    }

    private fun deleteExpense(expense: Expense){
        deletedExpense = expense
        oldExpenses = expenses
        Toast.makeText(this, "Expense deleted.", Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            db.expenseDao().delete(expense)

            expenses = expenses.filter { it.id != expense.id }
            runOnUiThread {}
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAllExp()
    }

}