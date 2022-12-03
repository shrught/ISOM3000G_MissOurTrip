package com.example.missourtrip

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddExpenseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var expenses:ArrayList<Expense>
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var categories:ArrayList<Category>
    private lateinit var catList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_add)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()

        catList = db.categoryDao().getName()


        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, catList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(exp_category)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@AddExpenseActivity
            prompt = "Select category"
            gravity = Gravity.CENTER

        }


        exp_save_btn.setOnClickListener{
            val name = exp_name.text.toString()
            val amount = exp_amount.text.toString().toDoubleOrNull()
            val date = exp_date.toString()
            val category = exp_category.getSelectedItem().toString()

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
                val expense = Expense(0, name, amount, category)
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }


}