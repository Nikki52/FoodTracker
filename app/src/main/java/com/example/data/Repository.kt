package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class Repository(private val db: AppDatabase, private val context: Context) {
    private val dataIngestionModule = DataIngestionModule(context, db.externalFoodDao())

    val allFoods: Flow<List<Food>> = db.foodDao().getAllFoods()
    
    fun searchFoods(query: String): Flow<List<Food>> = db.foodDao().searchFoods(query)

    fun searchExternalFoods(query: String): Flow<List<ExternalFoodData>> = db.externalFoodDao().searchExternalFoods(query)

    suspend fun insertFoods(foods: List<Food>) = db.foodDao().insertFoods(foods)

    fun getLogsForDate(date: Long): Flow<List<LogEntry>> = db.logEntryDao().getLogsForDate(date)
    
    suspend fun insertLog(logEntry: LogEntry) = db.logEntryDao().insertLog(logEntry)
    
    suspend fun deleteLogById(id: Int) = db.logEntryDao().deleteLogById(id)

    fun getWaterForDate(date: Long): Flow<Int?> = db.waterDao().getWaterForDate(date)
    
    suspend fun insertWater(waterEntry: WaterEntry) = db.waterDao().insertWater(waterEntry)
    
    suspend fun deleteLastWaterEntry(date: Long) = db.waterDao().deleteLastWaterEntry(date)

    fun getGoal(): Flow<UserGoal?> = db.goalDao().getGoal()
    
    suspend fun insertGoal(goal: UserGoal) = db.goalDao().insertGoal(goal)
    
    suspend fun initializeDbIfEmpty() {
        if (db.foodDao().getFoodCount() == 0) {
            db.goalDao().insertGoal(UserGoal()) // default goal
            db.foodDao().insertFoods(PrepopulatedData.foods)
        }
        dataIngestionModule.importExternalDataIfNeeded()
    }
}
