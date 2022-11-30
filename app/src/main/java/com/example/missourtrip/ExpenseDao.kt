package com.example.missourtrip

import androidx.room.*

@Dao
interface ExpenseDao {
    @Query("SELECT * from expenses")
    fun getAll(): List<Expense>

    @Insert
    fun insertAll(vararg expense: Expense)

    @Delete
    fun delete(expense: Expense)

    @Update
    fun update(vararg expense: Expense)
}