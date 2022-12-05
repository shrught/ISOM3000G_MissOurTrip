package com.example.missourtrip

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val name: String,
    val description: String):Serializable{

}
