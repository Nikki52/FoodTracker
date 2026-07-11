package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_entries")
data class WaterEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // Start of day in millis
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)
