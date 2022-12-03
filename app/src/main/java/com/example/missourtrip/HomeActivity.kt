package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.widget.Button
import java.io.File


class HomeActivity : AppCompatActivity() {

    private lateinit var btnExchangeRate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val path = filesDir
        val saved_password = File(path, "password.txt")

        btnExchangeRate = findViewById(R.id.to_exchange_rates_btn)

        btnExchangeRate.setOnClickListener{
            startActivity(Intent(this, RateActivity::class.java))
        }
    }

    fun toResetPassword(view: View){
        val ResetPasswordIntent = Intent(this, ResetPasswordActivity::class.java).apply {

        }
        startActivity(ResetPasswordIntent)
    }

    fun toCategories(view: View){
        val categoriesIntent = Intent(this, CategoryMain::class.java).apply {  }
        startActivity(categoriesIntent)
    }
}