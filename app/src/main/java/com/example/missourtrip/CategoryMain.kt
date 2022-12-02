package com.example.missourtrip

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_category_add.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_category_main.*
import java.io.File

class CategoryMain : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryArrayList: ArrayList<Category>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_main)

        categoryRecyclerView = findViewById(R.id.categoryList)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.setHasFixedSize(true)
        

    }



    fun toAddCategory(view: View){
        val addCategoryIntent = Intent(this, CategoryAdd::class.java).apply {  }
        startActivity(addCategoryIntent)
    }
}