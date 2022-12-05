package com.example.missourtrip

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Expense::class, Category::class], version = 5)
abstract class AppDatabase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
}

val MIGRATION_1_2 = object : Migration(2, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // database.execSQL(...)
    }
}



