package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Food
import com.example.data.LogEntry
import com.example.data.Repository
import com.example.data.UserGoal
import com.example.data.WaterEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

import kotlinx.coroutines.flow.combine

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = Repository(db, application)

    // Current Date logic
    private val _currentDate = MutableStateFlow(getStartOfDayMillis())
    val currentDate: StateFlow<Long> = _currentDate

    init {
        viewModelScope.launch {
            repository.initializeDbIfEmpty()
        }
    }

    // Goals
    val userGoal: StateFlow<UserGoal?> = repository.getGoal().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    // Logs for current date
    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyLogs: StateFlow<List<LogEntry>> = _currentDate.flatMapLatest { date ->
        repository.getLogsForDate(date)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Water for current date
    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyWaterMl: StateFlow<Int> = _currentDate.flatMapLatest { date ->
        repository.getWaterForDate(date).map { it ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Food>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.allFoods
        } else {
            repository.searchFoods(query).combine(repository.searchExternalFoods(query)) { local, external ->
                val mappedExt = external.map { ext ->
                    Food(
                        id = -ext.id, // Negative to distinguish from local if needed
                        name = "${ext.name} (${ext.source})",
                        servingSize = ext.servingSize,
                        calories = ext.calories,
                        protein = ext.protein,
                        carbs = ext.carbs,
                        fat = ext.fat,
                        fiber = ext.fiber,
                        category = ext.category
                    )
                }
                local + mappedExt
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun createAndLogCustomFood(
        name: String,
        servingSize: String,
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float,
        fiber: Float,
        category: String,
        mealType: String
    ) {
        viewModelScope.launch {
            val food = Food(
                name = name,
                servingSize = servingSize,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                fiber = fiber,
                category = category
            )
            // Ideally we insert and get ID, but for simplicity we can insert to DB then log
            // Since our DAO doesn't return ID directly on insert yet, we'll insert to DB
            repository.insertFoods(listOf(food)) // assuming we have insertFoods which takes a list
            
            // Log it directly (foodId will be 0 but name is what matters mostly for logs)
            logFood(food, mealType, 1.0f)
        }
    }

    // Add log
    fun logFood(food: Food, mealType: String, quantityMultiplier: Float) {
        viewModelScope.launch {
            repository.insertLog(
                LogEntry(
                    foodId = food.id,
                    foodName = food.name,
                    mealType = mealType,
                    date = _currentDate.value,
                    quantityMultiplier = quantityMultiplier,
                    calories = (food.calories * quantityMultiplier).toInt(),
                    protein = food.protein * quantityMultiplier,
                    carbs = food.carbs * quantityMultiplier,
                    fat = food.fat * quantityMultiplier,
                    fiber = food.fiber * quantityMultiplier
                )
            )
        }
    }

    fun deleteLog(logEntry: LogEntry) {
        viewModelScope.launch {
            repository.deleteLogById(logEntry.id)
        }
    }

    // Add water
    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.insertWater(
                WaterEntry(date = _currentDate.value, amountMl = amountMl)
            )
        }
    }

    // Undo last water
    fun undoLastWater() {
        viewModelScope.launch {
            repository.deleteLastWaterEntry(_currentDate.value)
        }
    }
    
    fun updateGoal(goal: UserGoal) {
        viewModelScope.launch {
            repository.insertGoal(goal)
        }
    }
    
    // Change date
    fun changeDateOffset(days: Int) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = _currentDate.value
            add(Calendar.DAY_OF_YEAR, days)
        }
        _currentDate.value = calendar.timeInMillis
    }

    private fun getStartOfDayMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

