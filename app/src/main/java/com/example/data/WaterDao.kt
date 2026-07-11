package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT SUM(amountMl) FROM water_entries WHERE date = :date")
    fun getWaterForDate(date: Long): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(waterEntry: WaterEntry)

    @Query("DELETE FROM water_entries WHERE id = (SELECT id FROM water_entries WHERE date = :date ORDER BY timestamp DESC LIMIT 1)")
    suspend fun deleteLastWaterEntry(date: Long)
}
