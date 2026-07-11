package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoal(
    @PrimaryKey val id: Int = 1, // Only one goal row
    val calories: Int = 2000,
    val protein: Int = 120,
    val carbs: Int = 250,
    val fat: Int = 65,
    val fiber: Int = 30,
    val waterMl: Int = 2500
)
