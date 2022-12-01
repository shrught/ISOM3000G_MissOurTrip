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

    fun to_reset_password(view: View){
        val reset_password_intent = Intent(this, ResetPasswordActivity::class.java).apply {

        }
        startActivity(reset_password_intent)
    }
}