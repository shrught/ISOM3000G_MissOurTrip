package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.widget.Button
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class HomeActivity : AppCompatActivity() {

    private lateinit var btnExchangeRate: Button
    private lateinit var db: AppDatabase
    private lateinit var catList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val path = filesDir
        val saved_password = File(path, "password.txt")

        btnExchangeRate = findViewById(R.id.to_exchange_rates_btn)

        btnExchangeRate.setOnClickListener{
            startActivity(Intent(this, RateActivity::class.java))
        }

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()

        catList = db.categoryDao().getName()

        if (catList.isEmpty()){
            val category = Category( "Food", "Food")
            insert(category)
        }
    }

    fun toResetPassword(view: View){
        val ResetPasswordIntent = Intent(this, ResetPasswordActivity::class.java).apply {

        }
        startActivity(ResetPasswordIntent)
    }

    fun toCategories(view: View){
        val categoriesIntent = Intent(this, CategoryActivity::class.java).apply {  }
        startActivity(categoriesIntent)
    }

    fun toExpense(view: View){
        val expenseIntent = Intent(this, ExpenseActivity::class.java).apply {  }
        startActivity(expenseIntent)
    }

    fun toStatistics(view: View){
        val statisticsIntent = Intent(this, StatisticsActivity::class.java).apply {  }
        startActivity(statisticsIntent)
    }
    
    private fun insert(category: Category){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").build()

        GlobalScope.launch {
            db.categoryDao().insertAll(category)
            finish()
        }
    }
}