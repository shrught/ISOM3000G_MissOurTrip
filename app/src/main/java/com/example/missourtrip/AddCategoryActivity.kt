package com.example.missourtrip

import android.content.Intent
import android.os.Bundle
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
                    warningAddCate.text = ""
                }
            }

            if (name.isEmpty()){
                warningAddCate.text = "Please enter a valid name"
            }
            else if (name in catList){
                warningAddCate.text = "Names of categories cannot be the same"
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