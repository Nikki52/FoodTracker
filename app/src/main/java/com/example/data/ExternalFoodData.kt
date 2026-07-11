package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "external_food_data")
data class ExternalFoodData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val servingSize: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float,
    val category: String,
    val source: String // e.g., "IFCT", "USDA", "Kaggle"
)
