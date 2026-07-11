package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoods(): Flow<List<Food>>

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :searchQuery || '%' OR category LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchFoods(searchQuery: String): Flow<List<Food>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)
    
    @Query("SELECT COUNT(*) FROM foods")
    suspend fun getFoodCount(): Int
}
