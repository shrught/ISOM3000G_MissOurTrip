package com.example.missourtrip

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val amount: Double,
    val currency: String,
    val date: String,
    val category: String,
    val split: Boolean
):Serializable {

}