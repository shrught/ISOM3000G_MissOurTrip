package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddExpenseActivity : AppCompatActivity() {
    private lateinit var expenses:ArrayList<Expense>
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_add)

        exp_save_btn.setOnClickListener{
            val name = exp_name.text.toString()
            val amount = exp_amount.text.toString().toDoubleOrNull()
            val date = exp_date.toString()

            exp_name.addTextChangedListener {
                if(it!!.count() > 0){
                    warningAddExp.text = ""
                }
            }
            exp_amount.addTextChangedListener {
                if(it!!.count() > 0){
                    warningAddExp.text = ""
                }
            }


            if (name.isEmpty()){
                warningAddExp.text = "Please enter a valid name"
            }
            else if (amount == null){
                warningAddExp.text = "Please enter a valid amount"
            }

            else{
                val expense = Expense(0, name, amount)
                insert(expense)
            }
        }

    }

    private fun insert(expense: Expense){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").build()
        GlobalScope.launch {
            db.expenseDao().insertAll(expense)
            finish()
        }
    }


}