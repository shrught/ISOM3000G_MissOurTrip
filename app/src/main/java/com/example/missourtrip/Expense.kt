package com.example.missourtrip

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    var amount: Double,
    val currency: String,
    val date: String,
    val category: String,
    val split: Int,
    val splitName: String,
    val settled: Boolean

):Serializable {

}