package com.example.missourtrip

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class, Category::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
}

