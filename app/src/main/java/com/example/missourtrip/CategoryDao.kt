package com.example.missourtrip

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * from categories")
    fun getAll(): List<Category>

    @Insert
    fun insertAll(vararg category: Category)

    @Delete
    fun delete(category: Category)

    @Update
    fun update(vararg category: Category)
}