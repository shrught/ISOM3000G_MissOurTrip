package com.example.missourtrip

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_add.*
import kotlinx.android.synthetic.main.activity_expense_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.util.*


class AddExpenseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var expense:Expense
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase
    private lateinit var categories:ArrayList<Category>
    private lateinit var catList: List<String>
    private lateinit var curencyList: List<String>
    val formatter = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_add)

        val dateView = findViewById<View>(R.id.exp_date) as TextView
        setDate(dateView)

        var editCheck: Boolean

        if (intent.getSerializableExtra("expense") != null) {
            expense = intent.getSerializableExtra("expense") as Expense
            editCheck = true
        }
        else{
            editCheck = false
        }

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()

        catList = db.categoryDao().getName()

        curencyList = listOf("HKD", "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "NZD")


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

        var cur = ArrayAdapter(this, android.R.layout.simple_spinner_item, curencyList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(exp_currency)
        {
            adapter = cur
            setSelection(0, false)
            onItemSelectedListener = this@AddExpenseActivity
            gravity = Gravity.CENTER

        }


        if (editCheck == true){
            expense = intent.getSerializableExtra("expense") as Expense
            exp_title.text = "Edit Expense"
            exp_name.setText(expense.name)
            exp_currency.setSelection(curencyList.indexOf(expense.currency))
            exp_amount.setText(expense.amount.toString())
            exp_category.setSelection(catList.indexOf(expense.category))
        }


        exp_save_btn.setOnClickListener{
            val name = exp_name.text.toString()
            val amount = exp_amount.text.toString().toDouble()
            val date = exp_date.text.toString()
            val category = exp_category.getSelectedItem().toString()
            val currency = exp_currency.getSelectedItem().toString()

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
            else if (currency == null){
                warningAddExp.text = "Please enter a valid amount"
            }
            else if (date == null){
                warningAddExp.text = "Please enter a valid amount"
            }
            else if (checkDate(date) == false){
                warningAddExp.text = "Please enter date in format - dd/mm/yyyy"
            }
            else if (category == null){
                warningAddExp.text = "Please enter a valid amount"
            }

            else{
                if (editCheck == false) {
                    val expenses = Expense(0, name, amount, currency, date, category, 1, "", false)
                    insert(expenses)
                }
                else{
                    val expenses = Expense(expense.id, name, amount, currency, date, category, 1, "", false)
                    update(expenses)
                }
            }

        }

        split_btn.setOnClickListener{
            val name = exp_name.text.toString()
            val amount = exp_amount.text.toString().toDoubleOrNull()
            val date = exp_date.text.toString()
            val category = exp_category.getSelectedItem().toString()
            val currency = exp_currency.getSelectedItem().toString()

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
            else if (currency == null){
                warningAddExp.text = "Please enter a valid amount"
            }
            else if (date == null){
                warningAddExp.text = "Please enter a valid amount"
            }
            else if (checkDate(date) == false){
                warningAddExp.text = "Please enter date in format - dd/mm/yyyy"
            }
            else if (category == null){
                warningAddExp.text = "Please enter a valid amount"
            }

            else{
                val intent = Intent(this, ExpenseSplitActivity::class.java).apply {
                    putExtra("name", name);
                    putExtra("amount", amount.toString());
                    putExtra("currency", currency);
                    putExtra("date", date);
                    putExtra("category", category);
                    if (editCheck == true) {
                        putExtra("expense", expense)
                    }
                }
                startActivity(intent)
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

    fun setDate(view: TextView) {
        val today = Calendar.getInstance().time //getting date
        val date: String = formatter.format(today)
        view.text = date
    }

    fun checkDate(date_input: String):Boolean{
        try {
            var date = formatter.parse(date_input)
            formatter.format(date)
        } catch (e: ParseException){
            return false;
        } catch (e: Exception){
            return false;
        }
        return true;
    }

    private fun update(expense: Expense){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").build()

        GlobalScope.launch {
            db.expenseDao().update(expense)
            finish()
        }
    }

}