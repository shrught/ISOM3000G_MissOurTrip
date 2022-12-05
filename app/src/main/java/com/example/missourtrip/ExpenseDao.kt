package com.example.missourtrip

import androidx.room.*
import java.time.temporal.TemporalAmount

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

    @Query("UPDATE expenses SET amount = :amount Where id = :id")
    fun updateAmountById(id: Int, amount: Double)

}