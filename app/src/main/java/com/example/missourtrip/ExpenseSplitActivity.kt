package com.example.missourtrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_add.*
import kotlinx.android.synthetic.main.activity_expense_split.*
import kotlinx.android.synthetic.main.activity_expense_split.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File.separator

class ExpenseSplitActivity : AppCompatActivity() {
    val amountList = ArrayList<TextView>()
    val nameList = ArrayList<EditText>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_split)

        val name = intent.getStringExtra("name")
        val amount = intent.getStringExtra("amount")
        val currency = intent.getStringExtra("currency")
        val date = intent.getStringExtra("date")
        val category = intent.getStringExtra("category")


        split_amount.text = amount

        var splitNo = 2

        var splitAmount = (amount!!.toBigDecimal().divide(splitNo.toBigDecimal())).toString()

        amount_me.text = splitAmount
        amount1.text = splitAmount

        add_parti_btn.setOnClickListener{
            val HLinear = LinearLayout(this)
            val newSpace = Space(this);
            val personName = EditText(this)
            val personAmount = TextView(this)

            newSpace.minimumWidth = 21
            HLinear.setOrientation(LinearLayout.HORIZONTAL)
            HLinear.minimumWidth = linear.width
            personName.minimumWidth = editTextTextPersonName.width

            linear.addView(newSpace)

            personName.hint = "Friend " + splitNo

            splitNo++

            splitAmount = (amount!!.toBigDecimal().divide(splitNo.toBigDecimal())).toDouble().toString()

            amount_me.text = splitAmount
            amount1.text = splitAmount

            amountList.add(personAmount)
            nameList.add(personName)

            HLinear.addView(personName)
            HLinear.addView(amountList[amountList.count()-1])

            for (i in 0..amountList.count()-1){
                println(amountList.count().toString() + "!!!!!!!!!!")
                amountList[i].text = splitAmount
                amountList[i].setTextSize("28".toFloat())
            }

            linear.addView(HLinear)
        }

        split_save.setOnClickListener {
            var splitName: String
            var snList: ArrayList<String> = ArrayList()
            var check:Boolean = false

            if (editTextTextPersonName.text.isNotEmpty()){
                check = true
            }
            else {
                check = false
                warningAddExp3.text = "Please enter a valid name"
            }
            if (nameList.count() > 0) {
                for (i in 0..nameList.count() - 1) {
                    if (nameList[i].text.isEmpty()) {
                        warningAddExp3.text = "Please enter valid names"
                        check = false
                        break
                    } else {
                        check = true
                    }
                }
            }
            if(check == true){

                snList.add(editTextTextPersonName.text.toString())

                for (i in 0.. nameList.count()-1){
                    snList.add(nameList[i].text.toString())
                }

                splitName = snList.joinToString(separator = ",")

                val expense = Expense(
                    0,
                    name.toString(),
                    amount.toDouble(),
                    currency.toString(),
                    date.toString(),
                    category.toString(),
                    splitNo,
                    splitName,
                    checkBox.isChecked
                )
                insert(expense)
                val intent = Intent(this, ExpenseActivity::class.java)
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
}