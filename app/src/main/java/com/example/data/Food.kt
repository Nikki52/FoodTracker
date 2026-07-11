package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val servingSize: String, // e.g., "1 cup", "100g", "1 piece"
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float,
    val category: String // "Indian", "Fruits", "Vegetables", etc.
)
