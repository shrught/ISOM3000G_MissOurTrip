package com.example.missourtrip

import android.content.Context
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.util.*


class AddExpenseActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var expenses:ArrayList<Expense>
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
//
//        var exp_date = TextView.AUTOFILL_TYPE_DATE(this)
//        exp_date.text = LocalDate.now()
        val dateView = findViewById<View>(R.id.exp_date) as TextView
        setDate(dateView)
//        var calendar = Calendar.getInstance();
//        val dateFormat = SimpleDateFormat("MM/dd/yyyy");
//        var date = dateFormat.parse(calendar.getTime().toString())
//        dateTimeDisplay.text(date)!!

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


        exp_save_btn.setOnClickListener{
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
                println("111111111111")
                warningAddExp.text = "Please enter date in format - dd/mm/yyyy"
            }
            else if (category == null){
                warningAddExp.text = "Please enter a valid amount"
            }

            else{
                val expense = Expense(0, name, amount,currency, date, category, split = false  )
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

}