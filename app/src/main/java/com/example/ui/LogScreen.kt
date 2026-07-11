package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    viewModel: MainViewModel,
    foodId: Int,
    mealType: String,
    onNavigateBack: () -> Unit
) {
    val foods by viewModel.searchResults.collectAsState()
    val food = foods.find { it.id == foodId }
    
    var quantity by remember { mutableStateOf("1.0") }
    
    if (food == null) {
        // Fallback if not found (should rarely happen unless db deleted)
        LaunchedEffect(Unit) { onNavigateBack() }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Food") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = food.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Category: ${food.category}", style = MaterialTheme.typography.bodyLarge)
            
            HorizontalDivider()
            
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Servings (${food.servingSize})") },
                modifier = Modifier.fillMaxWidth()
            )
            
            val qtyFloat = quantity.toFloatOrNull() ?: 0f
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Macros:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Calories: ${(food.calories * qtyFloat).toInt()} kcal")
                    Text("Protein: ${String.format("%.1f", food.protein * qtyFloat)} g")
                    Text("Carbs: ${String.format("%.1f", food.carbs * qtyFloat)} g")
                    Text("Fat: ${String.format("%.1f", food.fat * qtyFloat)} g")
                    Text("Fiber: ${String.format("%.1f", food.fiber * qtyFloat)} g")
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (qtyFloat > 0) {
                        viewModel.logFood(food, mealType, qtyFloat)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to $mealType")
            }
        }
    }
}
