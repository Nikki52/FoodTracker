package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_entries")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodId: Int, // Refers to Food.id (could use foreign key, but keeping simple for offline DB that might be pre-populated or custom)
    val foodName: String,
    val mealType: String, // "Breakfast", "Lunch", "Dinner", "Snacks"
    val date: Long, // Start of day in millis
    val quantityMultiplier: Float, // E.g. if serving is 100g and they ate 200g, this is 2.0
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float
)
