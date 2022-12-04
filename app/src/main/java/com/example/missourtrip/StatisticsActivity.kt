package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Space
import android.widget.TableRow
import android.widget.TextView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_statistics.*

class StatisticsActivity : AppCompatActivity() {
    private lateinit var categoryDB: AppDatabase
    private lateinit var expenseDB: AppDatabase
    private lateinit var summary:ArrayList<Summary>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        categoryDB = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()

        expenseDB = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").allowMainThreadQueries().build()

        calculateStatistics()
        displayStatistics()
    }

    data class Summary (val category: String, var total: Double = 0.0)

    private fun calculateStatistics() {
        summary = ArrayList()

        for (category in categoryDB.categoryDao().getName()) {
            summary.add(Summary(category))
        }

        for (expense in expenseDB.expenseDao().getAll()) {
            val categoryIndex = summary.indexOfFirst { it.category == expense.category }
            // category not found - should not occur
            if (categoryIndex == -1) {
                Log.d("Invalid category", expense.category)
                summary.add(Summary(expense.category, expense.amount))
            } else {
                summary[categoryIndex].total += expense.amount
            }
        }
        Log.d("summary", summary.joinToString())
    }

    private fun displayStatistics() {
        summary_table.removeAllViews()

        val newRows = ArrayList<TableRow>()
        val newSpaces = ArrayList<Space>()
        val newTextViews = ArrayList<ArrayList<TextView>>()

        val categoryNameIndex = 0
        val categoryTotalIndex = 1
        for ((row, value) in summary.withIndex()) {
            newTextViews.add(ArrayList<TextView>())
            newTextViews[row].add(TextView(this))
            newTextViews[row][categoryNameIndex].text = value.category
            newTextViews[row][categoryNameIndex].maxWidth = 400
            newTextViews[row].add(TextView(this))
            newTextViews[row][categoryTotalIndex].text = "$%.2f".format(value.total)
            newTextViews[row][categoryTotalIndex].maxWidth = 400

            newSpaces.add(Space(this))
            newSpaces[row].minimumWidth = 115

            newRows.add(TableRow(this))
            newRows[row].addView(newTextViews[row][categoryNameIndex])
            newRows[row].addView(newSpaces[row])
            newRows[row].addView(newTextViews[row][categoryTotalIndex])
            summary_table.addView(newRows[row])
        }
        var totalExpenses = 0.0
        summary.forEach { totalExpenses += it.total }
        total_expense.text = "$%.2f".format(totalExpenses)
        total_expense.maxWidth = 100
    }
}