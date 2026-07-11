package com.example.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStreamReader

class DataIngestionModule(
    private val context: Context,
    private val externalFoodDao: ExternalFoodDao
) {
    suspend fun importExternalDataIfNeeded() {
        withContext(Dispatchers.IO) {
            try {
                val count = externalFoodDao.getCount()
                val inputStream = context.assets.open("external_foods.json")
                val jsonString = InputStreamReader(inputStream).readText()
                val jsonArray = JSONArray(jsonString)

                if (count != jsonArray.length()) {
                    externalFoodDao.clearAll() // We need a clear method, or we can use a query

                    val foodsToInsert = mutableListOf<ExternalFoodData>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        foodsToInsert.add(
                            ExternalFoodData(
                                name = obj.getString("name"),
                                servingSize = obj.getString("servingSize"),
                                calories = obj.getInt("calories"),
                                protein = obj.getDouble("protein").toFloat(),
                                carbs = obj.getDouble("carbs").toFloat(),
                                fat = obj.getDouble("fat").toFloat(),
                                fiber = obj.getDouble("fiber").toFloat(),
                                category = obj.getString("category"),
                                source = obj.optString("source", "Unknown")
                            )
                        )
                    }
                    externalFoodDao.insertExternalFoods(foodsToInsert)
                    Log.d("DataIngestion", "Successfully imported ${foodsToInsert.size} external foods.")
                }
            } catch (e: Exception) {
                Log.e("DataIngestion", "Failed to import external foods", e)
            }
        }
    }
}
