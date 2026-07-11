package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogEntryDao {
    @Query("SELECT * FROM log_entries WHERE date = :date ORDER BY mealType ASC")
    fun getLogsForDate(date: Long): Flow<List<LogEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(logEntry: LogEntry)

    @Query("DELETE FROM log_entries WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}
