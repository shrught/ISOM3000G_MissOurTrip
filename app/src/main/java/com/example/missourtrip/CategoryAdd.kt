package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.missourtrip.databinding.ActivityCategoryAddBinding
import com.example.missourtrip.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_category_add.*


class CategoryAdd : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryAddBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_add)

        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        database = Firebase.database.reference

        val cancelBtn: Button = CancelCategoryAddBtn;
        cancelBtn.setOnClickListener {
            onBackPressed()
        }

        categoryAddBtn.setOnClickListener {
            val categoryName = category_add_name_input.text.toString()
            val categoryDescription = category_add_description_input.text.toString()



            val newCategory = Category(categoryName, categoryDescription)
            database.child("Categories").child(categoryName).setValue(newCategory).addOnSuccessListener {
                category_add_name_input.text.clear()
                category_add_description_input.text.clear()

                Toast.makeText(this, "Category Added!", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
        }



    }

    //fun addNewCategory(view: View){



    //}
}