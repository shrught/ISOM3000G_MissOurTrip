package com.example.missourtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_category_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class CategoryActivity : AppCompatActivity() {
    private lateinit var deletedCategory: Category
    private lateinit var categories: List<Category>
    private lateinit var expenses: List<Expense>
    private lateinit var oldCategories: List<Category>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var linearLayoutManager_cat: LinearLayoutManager
    private lateinit var db_cat: AppDatabase
    private lateinit var db_exp: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_main)

        categories = arrayListOf()

        categoryAdapter = CategoryAdapter(categories)
        linearLayoutManager_cat = LinearLayoutManager(this)

        db_exp= Room.databaseBuilder(this,
            AppDatabase::class.java,
            "expenses").allowMainThreadQueries().fallbackToDestructiveMigration().build()


        db_cat = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").allowMainThreadQueries().fallbackToDestructiveMigration().build()

        expenses = db_exp.expenseDao().getAll()

        recycleView_cat.apply {
            adapter = categoryAdapter
            layoutManager = linearLayoutManager_cat
        }
        recycleView_cat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        //swipe to remove
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletedCategory(categories[viewHolder.adapterPosition])
                }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recycleView_cat)


        cate_add_btn.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAllCate(){
        GlobalScope.launch {
            categories = db_cat.categoryDao().getAll()
            runOnUiThread{
                categoryAdapter.setData(categories)
            }
        }
    }



    private fun deletedCategory(category: Category){
        deletedCategory = category
        oldCategories = categories
        expenses = expenses.filter { it.category == category.name}
        if (expenses.isEmpty()) {
            Toast.makeText(this, "Category deleted.", Toast.LENGTH_SHORT).show()

            GlobalScope.launch {

                db_cat.categoryDao().delete(category)
                categories = categories.filter { it.name != category.name}
                runOnUiThread {}
            }
        }
        else{
            Toast.makeText(this, "This category cannot be deleted. Please delete all related expenses first.", Toast.LENGTH_SHORT).show()
            fetchAllCate()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAllCate()
    }

}