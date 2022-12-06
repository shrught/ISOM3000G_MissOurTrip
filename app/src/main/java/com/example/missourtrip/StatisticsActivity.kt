package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TableRow
import android.widget.TextView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_statistics.*

data class Summary (val category: String, var currencies: ArrayList<CurrencySummary> = ArrayList())
data class CurrencySummary (val currency: String, var total: Double = 0.0)

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
        Log.d("summary", summary.joinToString())
    }

    private fun displayStatistics() {
        summary_table.removeAllViews()

        val newRows = ArrayList<TableRow>()
        val newSpaces = ArrayList<Space>()
        val newCategoryNames = ArrayList<TextView>()
        val newLinearLayout = ArrayList<LinearLayout>()
        val newCurrencyTotals = ArrayList<ArrayList<TextView>>()

        for ((row, value) in summary.withIndex()) {
            newCategoryNames.add(TextView(this))
            newCategoryNames[row].text = value.category
            newCategoryNames[row].width = 400

            // list of currency totals
            newLinearLayout.add(LinearLayout(this))
            newLinearLayout[row].orientation = LinearLayout.VERTICAL
            newCurrencyTotals.add(ArrayList<TextView>())
            for ((col, value) in summary[row].currencies.withIndex()) {
                newCurrencyTotals[row].add(TextView(this))
                newCurrencyTotals[row][col].text = "%s %.2f".format(value.currency, value.total)
                newCurrencyTotals[row][col].maxWidth = 400
                newLinearLayout[row].addView(newCurrencyTotals[row][col])
                Log.d("currencies summary", "$col, $value")
            }

            if (summary[row].currencies.size == 0) {
                newCurrencyTotals[row].add(TextView(this))
                newCurrencyTotals[row][0].text = "-"
                newCurrencyTotals[row][0].maxWidth = 400
                newLinearLayout[row].addView(newCurrencyTotals[row][0])
            }

            newSpaces.add(Space(this))
            newSpaces[row].minimumWidth = 100

            newRows.add(TableRow(this))
            newRows[row].addView(newCategoryNames[row])
            newRows[row].addView(newSpaces[row])
            newRows[row].addView(newLinearLayout[row])
            summary_table.addView(newRows[row])
        }

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