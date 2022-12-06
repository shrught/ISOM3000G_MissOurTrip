package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_main.*
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.activity_statistics.recycleView_exp

data class Summary (val category: String, var currencies: ArrayList<CurrencySummary> = ArrayList())
data class CurrencySummary (val currency: String, var total: Double = 0.0)

class StatisticsActivity : AppCompatActivity() {
    private lateinit var categoryDB: AppDatabase
    private lateinit var expenseDB: AppDatabase
    private lateinit var summary:ArrayList<Summary>
    private lateinit var statisticsAdapter: StatisticsAdapter
    private lateinit var linearLayoutManager_exp: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        
        categoryDB = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()
        
        expenseDB = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").allowMainThreadQueries().build()
        
        summary = arrayListOf()
        linearLayoutManager_exp = LinearLayoutManager(this)
        
        statisticsAdapter = StatisticsAdapter(summary)
        recycleView_exp.apply {
            adapter = statisticsAdapter
            layoutManager = linearLayoutManager_exp
        }
        recycleView_exp.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        calculateStatistics()
        displayStatistics()
    }

    private fun calculateStatistics() {
        for (category in categoryDB.categoryDao().getName()) {
            summary.add(Summary(category))
        }

        for (expense in expenseDB.expenseDao().getAll()) {
            val categoryIndex = summary.indexOfFirst { it.category == expense.category }
            // category not found - should not occur
            if (categoryIndex == -1) {
                Log.d("Invalid category", expense.category)
                val newCurr = CurrencySummary(expense.currency, expense.amount)
                summary.add(Summary(expense.category, arrayListOf(newCurr)))
            } else {
                val currIndex = summary[categoryIndex].currencies.indexOfFirst { it.currency == expense.currency }
                if (currIndex == -1) {
                    val newCurr = CurrencySummary(expense.currency, expense.amount)
                    summary[categoryIndex].currencies.add(newCurr)
                } else {
                    summary[categoryIndex].currencies[currIndex].total += expense.amount
                }
            }
        }
        summary.sortByDescending{ it.currencies.size }
        Log.d("summary", summary.joinToString())
    }

    private fun displayStatistics() {
        statisticsAdapter.setData(summary)
        var summaryCurrencies = summary.map { it.currencies }.flatten().groupBy {it.currency }
            .map { (key,value) ->
                var total = 0.0
                value.forEach{ total += it.total}
                CurrencySummary(key, total)
            }
        var newTotalExpenses = ArrayList<TextView>()
        for ((row, value) in summaryCurrencies.withIndex()) {
            newTotalExpenses.add(TextView(this))
            newTotalExpenses[row].text = "%s %.2f".format(value.currency, value.total)
            newTotalExpenses[row].maxWidth = 100
            total_expenses.addView(newTotalExpenses[row])
        }
    }
}