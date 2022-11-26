package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }



    fun signup(view: View){

        val path = filesDir
        val saved_password = File(path, "password.txt");

        if(saved_password.readText() == ""){
            var user_password = ""
            user_password = signup_password.text.toString()

            if(user_password == ""){
                val reminder = Snackbar.make(view, "Password can't be null!", Snackbar.LENGTH_SHORT)
                reminder.show();
            }
            else{
                saved_password.writeText(user_password)
                val intent = Intent(this, LoginActivity::class.java).apply{
                    putExtra("user_password", user_password);
                }
                startActivity(intent)
            }

        }
        else{
            val reminder = Snackbar.make(view, "You have signed up before! Please Login", Snackbar.LENGTH_SHORT)
            reminder.show();
        }


    }

    fun to_login(view: View){
        val path = filesDir
        val saved_password = File(path, "password.txt");
        var user_password = saved_password.readText()

        val intent = Intent(this, LoginActivity::class.java).apply{
            putExtra("user_password", user_password);
        }
        startActivity(intent)
    }




}