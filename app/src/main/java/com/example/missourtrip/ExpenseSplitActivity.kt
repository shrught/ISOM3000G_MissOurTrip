package com.example.missourtrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_expense_split.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode

class ExpenseSplitActivity : AppCompatActivity() {
    val amountList = ArrayList<TextView>()
    val nameList = ArrayList<EditText>()

    private lateinit var expense:Expense
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_split)

        val name = intent.getStringExtra("name")
        val amount = intent.getStringExtra("amount")
        val currency = intent.getStringExtra("currency")
        val date = intent.getStringExtra("date")
        val category = intent.getStringExtra("category")

        var editCheck: Boolean

        var splitNo = 2

        split_amount.text = amount

        if (intent.getSerializableExtra("expense") != null) {
            expense = intent.getSerializableExtra("expense") as Expense
            println(expense)
            editCheck = true
        }
        else{
            editCheck = false
        }

        if (editCheck == true && expense.split > 1){
            val NameArray = ArrayList(expense.splitName.split(","))
            var splitAmount = expense.amount.toBigDecimal().divide(expense.split.toBigDecimal(),2, RoundingMode.HALF_UP )

            amount_me.text = splitAmount.toString()
            amount1.text = splitAmount.toString()
            checkBox.isChecked = expense.settled


            Name.setText(NameArray[0].toString())
            NameArray.removeAt(0)

            splitNo = expense.split

            for (i in 0 .. NameArray.count()-1){
                val HLinear = LinearLayout(this)
                val newSpace = Space(this);
                val personName = EditText(this)
                val personAmount = TextView(this)

                newSpace.minimumWidth = 21
                HLinear.setOrientation(LinearLayout.HORIZONTAL)
                HLinear.minimumWidth = linear.width
                personName.minimumWidth = 447
                personAmount.setTextSize("28".toFloat())

                linear.addView(newSpace)
                personName.setText(NameArray[i])
                personAmount.setText(splitAmount.toString())

                amountList.add(personAmount)
                nameList.add(personName)

                HLinear.addView(personName)
                HLinear.addView(amountList[amountList.count() - 1])

                for (i in 0..amountList.count() - 1) {
                    amountList[i].text = splitAmount.toString()
                    amountList[i].setTextSize("28".toFloat())
                    nameList[i].minimumWidth = 447
                }
                linear.addView(HLinear)
            }
        }
        else {

            var samount = amount!!.toBigDecimal()

            var splitAmount = samount.divide(splitNo.toBigDecimal(), 2, RoundingMode.HALF_UP)

            amount_me.text = splitAmount.toString()
            amount1.text = splitAmount.toString()
        }

        add_parti_btn.setOnClickListener {
            val HLinear = LinearLayout(this)
            val newSpace = Space(this);
            val personName = EditText(this)
            val personAmount = TextView(this)

            newSpace.minimumWidth = 21
            HLinear.setOrientation(LinearLayout.HORIZONTAL)
            HLinear.minimumWidth = linear.width
            personName.minimumWidth = 447

            linear.addView(newSpace)

            personName.hint = "Friend " + splitNo

            splitNo++

            var splitAmount =
                amount!!.toBigDecimal().divide(splitNo.toBigDecimal(), 2, RoundingMode.HALF_UP)

            amount_me.text = splitAmount.toString()
            amount1.text = splitAmount.toString()

            amountList.add(personAmount)
            nameList.add(personName)

            HLinear.addView(personName)
            HLinear.addView(amountList[amountList.count() - 1])

            for (i in 0..amountList.count() - 1) {
                amountList[i].text = splitAmount.toString()
                amountList[i].setTextSize("28".toFloat())
            }
            linear.addView(HLinear)
        }

        split_save.setOnClickListener {
            var splitName: String
            var snList: ArrayList<String> = ArrayList()
            var check:Boolean = false

            if (Name.text.isNotEmpty()){
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

                snList.add(Name.text.toString())

                for (i in 0.. nameList.count()-1){
                    snList.add(nameList[i].text.toString())
                }

                splitName = snList.joinToString(separator = ",")

                if (editCheck == false) {
                    val expense = Expense(
                        0,
                        name.toString(),
                        amount!!.toDouble(),
                        currency.toString(),
                        date.toString(),
                        category.toString(),
                        splitNo,
                        splitName,
                        checkBox.isChecked
                    )
                    insert(expense)
                }
                else{
                    var settledAmount = (amount!!.toDouble())/splitNo
                    if(checkBox.isChecked){
                        val expense = Expense(
                            expense.id,
                            name.toString(),
                            settledAmount.toDouble(),
                            currency.toString(),
                            date.toString(),
                            category.toString(),
                            splitNo,
                            splitName,
                            checkBox.isChecked
                        )
                        update(expense)

                    }
                    else{
                        val expense = Expense(
                            expense.id,
                            name.toString(),
                            amount!!.toDouble(),
                            currency.toString(),
                            date.toString(),
                            category.toString(),
                            splitNo,
                            splitName,
                            checkBox.isChecked
                        )
                        update(expense)

                    }

                }
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