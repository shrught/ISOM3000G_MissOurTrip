package com.example.missourtrip

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Expense::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao
}