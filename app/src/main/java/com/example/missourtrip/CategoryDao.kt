package com.example.missourtrip

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * from categories")
    fun getAll(): List<Category>

    @Query("SELECT name from categories")
    fun getName(): List<String>

    @Insert
    fun insertAll(vararg category: Category)

    @Delete
    fun delete(category: Category)

    @Update
    fun update(vararg category: Category)
}