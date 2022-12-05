package com.example.missourtrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_category_add.*
import kotlinx.android.synthetic.main.activity_category_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class editCategory : AppCompatActivity(){
    private lateinit var category: Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_add)

        val name = intent.getStringExtra("category_name")
        val description = intent.getStringExtra("category_des")

        cate_title.text = "Edit Category"

        cate_name.isEnabled = false

        cate_name.setText(name)
        cate_desc.setText(description)

        cate_save_btn.setOnClickListener {
            var Newdescription = cate_desc.text.toString()


            val category = Category( name.toString(), Newdescription)
            println(category)
            update(category)
            }

        cate_cancel_btn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }


    private fun update(category: Category){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").build()

        GlobalScope.launch {
            db.categoryDao().update(category)
            finish()
        }
    }

}