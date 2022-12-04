package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_expense_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class ExpenseActivity : AppCompatActivity() {
    private lateinit var deletedExpense: Expense
    private lateinit var expenses: List<Expense>
    private lateinit var oldExpenses: List<Expense>
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager_exp: LinearLayoutManager
    private lateinit var db: AppDatabase

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