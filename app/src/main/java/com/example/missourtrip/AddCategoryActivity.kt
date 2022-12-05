package com.example.missourtrip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_category_add.*
import kotlinx.android.synthetic.main.activity_expense_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var catList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_add)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().build()

        catList = db.categoryDao().getName()

        cate_save_btn.setOnClickListener {
            val name = cate_name.text.toString()
            val description = cate_desc.text.toString()

            cate_name.addTextChangedListener {
                if(it!!.count() > 0){
                }
            }

            if (name.isEmpty()){
                Toast.makeText(this, "Please enter a valid name.", Toast.LENGTH_SHORT).show()
            }
            else if (name in catList){
                Toast.makeText(this, "Names of categories cannot be the same.", Toast.LENGTH_SHORT).show()
            }
            else {

                val category = Category( name, description)

                println(category)
                insert(category)
            }
        }

        cate_cancel_btn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

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