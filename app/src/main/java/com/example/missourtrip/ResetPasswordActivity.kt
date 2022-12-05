package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_reset_password.*
import java.io.File

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
    }

    fun reset_password(view: View){
        val path = filesDir
        val saved_password = File(path, "password.txt")

        if(current_password.text.toString() == saved_password.readText()){

            if(new_password.text == null){
                Toast.makeText(this, "Password Can't be Null!!",Toast.LENGTH_SHORT).show()
            }
            else{
                saved_password.writeText(new_password.text.toString());
                val reset_successful = Snackbar.make(view, "Password Reset Successful, Please Login again!!", Snackbar.LENGTH_SHORT)
                reset_successful.show();

                val to_login_intent = Intent(this, LoginActivity::class.java).apply {

                }
                startActivity(to_login_intent)

            }


        }
        else{
            Toast.makeText(this, "Current Password is Wrong!!",Toast.LENGTH_SHORT).show()
        }

    }
}