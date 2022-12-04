package com.example.missourtrip

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.missourtrip.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_category_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.DividerItemDecoration


//import com.ebookfrenzy.commongestures.databinding.ActivityMainBinding


class CategoryActivity : AppCompatActivity(){
    private lateinit var categories: List<Category>
    private lateinit var oldCategories: List<Category>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var linearLayoutManager_cat: LinearLayoutManager
    private lateinit var db: AppDatabase
    private var toast: Toast? = null
//    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_main)

        // In our case, we tap on Text View

        categories = arrayListOf()

        categoryAdapter = CategoryAdapter(categories)
        linearLayoutManager_cat = LinearLayoutManager(this)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "categories").fallbackToDestructiveMigration().build()


        recycleView_cat.apply {
            adapter = categoryAdapter
            layoutManager = linearLayoutManager_cat
        }
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

//
//        //swipe to remove
//        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                deletedCategory(categories[viewHolder.adapterPosition])
//            }
//
//            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//                super.onSelectedChanged(viewHolder, actionState)
//            }
//
//        }
//
//        val swipeHelper = ItemTouchHelper(itemTouchHelper)
//        swipeHelper.attachToRecyclerView(recycleView_cat)

//        recycleView_cat.adapter = RecyclerView.Adapter(
//            listOf(
//                "Item 0: No action",
//                "Item 1: Delete",
//                "Item 2: Delete & Mark as unread",
//                "Item 3: Delete, Mark as unread & Archive"
//            )
//        )
        recycleView_cat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        recycleView_cat.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recycleView_cat) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                val markAsUnreadButton = markAsUnreadButton(position)
                val archiveButton = archiveButton(position)
                when (position) {
                    1 -> buttons = listOf(deleteButton)
                    2 -> buttons = listOf(deleteButton, markAsUnreadButton)
                    3 -> buttons = listOf(deleteButton, markAsUnreadButton, archiveButton)
                    else -> Unit
                }
                return buttons
            }


        })


        itemTouchHelper.attachToRecyclerView(recycleView_cat)

//        cate_add_btn.setOnClickListener {
//            val intent = Intent(this, AddCategoryActivity::class.java)
//            startActivity(intent)
//        }


    }

    private fun toast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    deletedCategory(categories[position])
//                }
            })
    }

    private fun markAsUnreadButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Mark as unread",
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    toast("Marked as unread item $position")
//                }
            })
    }

    private fun archiveButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Archive",
            14.0f,
            android.R.color.holo_blue_light,
            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    toast("Archived item $position")
//                }
            })
    }


    private fun fetchAllCate(){
        GlobalScope.launch {
            categories = db.categoryDao().getAll()
            runOnUiThread{
                categoryAdapter.setData(categories)
            }
        }
    }

    private fun deletedCategory(category: Category){
//        deletedCategory = category
        oldCategories = categories

        GlobalScope.launch {
            db.categoryDao().delete(category)

            categories = categories.filter { it.name != category.name }
            runOnUiThread {}
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAllCate()
    }



}

