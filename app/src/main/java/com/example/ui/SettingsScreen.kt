package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.UserGoal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val userGoal by viewModel.userGoal.collectAsState()
    
    var caloriesStr by remember(userGoal) { mutableStateOf(userGoal?.calories?.toString() ?: "2000") }
    var proteinStr by remember(userGoal) { mutableStateOf(userGoal?.protein?.toString() ?: "120") }
    var carbsStr by remember(userGoal) { mutableStateOf(userGoal?.carbs?.toString() ?: "250") }
    var fatStr by remember(userGoal) { mutableStateOf(userGoal?.fat?.toString() ?: "65") }
    var fiberStr by remember(userGoal) { mutableStateOf(userGoal?.fiber?.toString() ?: "30") }
    var waterStr by remember(userGoal) { mutableStateOf(userGoal?.waterMl?.toString() ?: "2500") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            Text("Nutritional Goals", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = caloriesStr,
                onValueChange = { caloriesStr = it },
                label = { Text("Calories (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = proteinStr,
                onValueChange = { proteinStr = it },
                label = { Text("Protein (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = carbsStr,
                onValueChange = { carbsStr = it },
                label = { Text("Carbs (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = fatStr,
                onValueChange = { fatStr = it },
                label = { Text("Fat (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = fiberStr,
                onValueChange = { fiberStr = it },
                label = { Text("Fiber (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = waterStr,
                onValueChange = { waterStr = it },
                label = { Text("Water (ml)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val updatedGoal = UserGoal(
                        id = userGoal?.id ?: 1,
                        calories = caloriesStr.toIntOrNull() ?: 2000,
                        protein = proteinStr.toIntOrNull() ?: 120,
                        carbs = carbsStr.toIntOrNull() ?: 250,
                        fat = fatStr.toIntOrNull() ?: 65,
                        fiber = fiberStr.toIntOrNull() ?: 30,
                        waterMl = waterStr.toIntOrNull() ?: 2500
                    )
                    viewModel.updateGoal(updatedGoal)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Goals")
            }
        }
    }
}
