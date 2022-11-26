package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import java.io.File


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun login(view: View){
        val path = filesDir
        val saved_password = File(path, "password.txt")

        val input_password = login_password.text.toString()
        if(input_password == saved_password.readText()){
            val intent = Intent(this, HomeActivity::class.java).apply {

            }
            startActivity(intent)
        }
        else{
            val reminder = Snackbar.make(view, "Wrong Password!!", Snackbar.LENGTH_SHORT)
            reminder.show();
        }

    }
}