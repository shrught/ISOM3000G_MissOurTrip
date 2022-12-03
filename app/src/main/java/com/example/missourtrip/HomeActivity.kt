package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import java.io.File


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val path = filesDir
        val saved_password = File(path, "password.txt")


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

    fun toExpenses(view: View){
        val expensesIntent = Intent(this, ExpenseActivity::class.java).apply {  }
        startActivity(expensesIntent)
    }
}