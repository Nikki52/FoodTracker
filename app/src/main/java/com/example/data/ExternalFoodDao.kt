package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExternalFoodDao {
    @Query("SELECT * FROM external_food_data WHERE name LIKE '%' || :searchQuery || '%' OR category LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchExternalFoods(searchQuery: String): Flow<List<ExternalFoodData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExternalFoods(foods: List<ExternalFoodData>)

    @Query("SELECT COUNT(*) FROM external_food_data")
    suspend fun getCount(): Int

    @Query("DELETE FROM external_food_data")
    suspend fun clearAll()
}
