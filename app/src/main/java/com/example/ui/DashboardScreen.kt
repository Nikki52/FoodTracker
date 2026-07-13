package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import com.example.data.LogEntry
import com.example.data.UserGoal
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToSearch: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val currentDateMillis by viewModel.currentDate.collectAsState()
    val dailyLogs by viewModel.dailyLogs.collectAsState()
    val userGoal by viewModel.userGoal.collectAsState()
    val waterMl by viewModel.dailyWaterMl.collectAsState()
    val userName by viewModel.userName.collectAsState()

    val dateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    val dateString = dateFormat.format(Date(currentDateMillis))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.changeDateOffset(-1) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.changeDateOffset(1) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val greeting = when (hour) {
                        in 0..11 -> "Good Morning,"
                        in 12..16 -> "Good Afternoon,"
                        else -> "Good Evening,"
                    }
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (userName.isNotBlank()) "$userName 👋" else "Guest 👋",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                userGoal?.let { goal ->
                    MacrosSummary(dailyLogs, goal)
                }
            }

            item {
                userGoal?.let { goal ->
                    WaterWidget(
                        waterMl = waterMl,
                        waterGoal = goal.waterMl,
                        onAddWater = viewModel::addWater,
                        onUndoWater = viewModel::undoLastWater
                    )
                }
            }

            val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snacks")
            mealTypes.forEach { mealType ->
                val meals = dailyLogs.filter { it.mealType == mealType }
                item {
                    MealSection(
                        mealType = mealType,
                        meals = meals,
                        onAddClick = { onNavigateToSearch(mealType) },
                        onDeleteClick = viewModel::deleteLog
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    var showSetupDialog by remember { mutableStateOf(false) }
    var setupStage by remember { mutableIntStateOf(1) }

    LaunchedEffect(userName) {
        if (userName.isBlank()) {
            showSetupDialog = true
            setupStage = 1
        }
    }

    if (showSetupDialog) {
        var tempName by remember { mutableStateOf("") }
        var calVal by remember { mutableStateOf("2000") }
        var protVal by remember { mutableStateOf("120") }
        var carbVal by remember { mutableStateOf("250") }
        var fatVal by remember { mutableStateOf("65") }
        var fibVal by remember { mutableStateOf("30") }
        var waterVal by remember { mutableStateOf("2500") }

        if (setupStage == 1) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Welcome to Food Tracker!") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Please enter your name to personalize your experience.")
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Your Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) {
                                setupStage = 2
                            }
                        },
                        enabled = tempName.isNotBlank()
                    ) {
                        Text("Next")
                    }
                }
            )
        } else {
            val dialogScrollState = rememberScrollState()
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Set Daily Goals") },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .verticalScroll(dialogScrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Hi $tempName! Set your daily nutrition and water goals.")
                        OutlinedTextField(
                            value = calVal,
                            onValueChange = { calVal = it },
                            label = { Text("Calories (kcal)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = protVal,
                            onValueChange = { protVal = it },
                            label = { Text("Protein (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = carbVal,
                            onValueChange = { carbVal = it },
                            label = { Text("Carbohydrates (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = fatVal,
                            onValueChange = { fatVal = it },
                            label = { Text("Fats (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = fibVal,
                            onValueChange = { fibVal = it },
                            label = { Text("Fiber (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = waterVal,
                            onValueChange = { waterVal = it },
                            label = { Text("Water (ml)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateUserName(tempName)
                            val goal = UserGoal(
                                id = userGoal?.id ?: 1,
                                calories = calVal.toIntOrNull() ?: 2000,
                                protein = protVal.toIntOrNull() ?: 120,
                                carbs = carbVal.toIntOrNull() ?: 250,
                                fat = fatVal.toIntOrNull() ?: 65,
                                fiber = fibVal.toIntOrNull() ?: 30,
                                waterMl = waterVal.toIntOrNull() ?: 2500
                            )
                            viewModel.updateGoal(goal)
                            showSetupDialog = false
                        }
                    ) {
                        Text("Get Started")
                    }
                }
            )
        }
    }
}

@Composable
fun MacrosSummary(logs: List<LogEntry>, goal: UserGoal) {
    val calories = logs.sumOf { it.calories }
    val protein = logs.sumOf { it.protein.toDouble() }.toFloat()
    val carbs = logs.sumOf { it.carbs.toDouble() }.toFloat()
    val fat = logs.sumOf { it.fat.toDouble() }.toFloat()
    val fiber = logs.sumOf { it.fiber.toDouble() }.toFloat()

    val percent = (calories.toFloat() / goal.calories).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Calories
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Calories", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$calories",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (calories <= goal.calories) {
                            "Remaining ${goal.calories - calories} kcal"
                        } else {
                            "Exceeded by ${calories - goal.calories} kcal"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (calories <= goal.calories) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                    )
                }
                CircularProgress(
                    progress = percent,
                    color = MaterialTheme.colorScheme.primary,
                    centerText = "${(percent * 100).toInt()}%\n"
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Macros in 2x2 grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MacroItem("Protein 💪", protein, goal.protein.toFloat(), MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                MacroItem("Carbs \uD83C\uDF5A", carbs, goal.carbs.toFloat(), MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MacroItem("Fat \uD83E\uDD51", fat, goal.fat.toFloat(), MaterialTheme.colorScheme.error, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                MacroItem("Fiber \uD83C\uDF3E", fiber, goal.fiber.toFloat(), Color(0xFF4CAF50), Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MacroItem(name: String, current: Float, goal: Float, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.labelLarge)
            if (current <= goal) {
                Text("${current.toInt()}/${goal.toInt()}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            } else {
                Text("${current.toInt()}/${goal.toInt()} (Exceeded)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (current / goal).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = color,
            trackColor = color.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round
        )
    }
}


@Composable
fun CircularProgress(progress: Float, color: Color, centerText: String) {
    val strokeWidth = 8.dp
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawArc(
                color = color.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = centerText,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun WaterWidget(waterMl: Int, waterGoal: Int, onAddWater: (Int) -> Unit, onUndoWater: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.WaterDrop, contentDescription = "Water", tint = Color(0xFF2196F3), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Water", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${waterMl / 1000f} L", style = MaterialTheme.typography.headlineMedium)
                    }
                    if (waterMl > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material3.TextButton(
                            onClick = onUndoWater,
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                        ) {
                            Text("Undo", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Goal", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${waterGoal / 1000f} L", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { (waterMl.toFloat() / waterGoal).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFF2196F3),
                trackColor = Color(0xFF2196F3).copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WaterButton("+250", onClick = { onAddWater(250) })
                WaterButton("+500", onClick = { onAddWater(500) })
                WaterButton("+750", onClick = { onAddWater(750) })
                WaterButton("+1L", onClick = { onAddWater(1000) })
            }
        }
    }
}

@Composable
fun WaterButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun MealSection(
    mealType: String,
    meals: List<LogEntry>,
    onAddClick: () -> Unit,
    onDeleteClick: (LogEntry) -> Unit
) {
    val totalCals = meals.sumOf { it.calories }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(mealType, style = MaterialTheme.typography.titleLarge)
                Text("$totalCals kcal", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            meals.forEach { meal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(meal.foodName, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "${meal.calories} kcal • P: ${String.format("%.1f", meal.protein)}g",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { onDeleteClick(meal) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Food")
                Spacer(modifier = Modifier.width(8.dp))
                Text("ADD FOOD")
            }
        }
    }
}
