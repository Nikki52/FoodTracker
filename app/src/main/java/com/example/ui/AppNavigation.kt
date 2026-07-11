package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToSearch = { mealType -> 
                    navController.navigate("search/$mealType")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("search/{mealType}") { backStackEntry ->
            val mealType = backStackEntry.arguments?.getString("mealType") ?: "Snacks"
            SearchScreen(
                viewModel = viewModel,
                mealType = mealType,
                onNavigateBack = { navController.popBackStack() },
                onFoodSelected = { foodId ->
                    navController.navigate("log/$mealType/$foodId")
                },
                onCreateCustomFood = {
                    navController.navigate("create_food/$mealType")
                }
            )
        }
        composable("create_food/{mealType}") { backStackEntry ->
            val mealType = backStackEntry.arguments?.getString("mealType") ?: "Snacks"
            CreateFoodScreen(
                viewModel = viewModel,
                mealType = mealType,
                onNavigateBack = {
                    navController.popBackStack("dashboard", false)
                }
            )
        }
        composable("log/{mealType}/{foodId}") { backStackEntry ->
            val mealType = backStackEntry.arguments?.getString("mealType") ?: "Snacks"
            val foodIdStr = backStackEntry.arguments?.getString("foodId") ?: "0"
            val foodId = foodIdStr.toIntOrNull() ?: 0
            
            LogScreen(
                viewModel = viewModel,
                foodId = foodId,
                mealType = mealType,
                onNavigateBack = { 
                    // Pop up to dashboard after logging
                    navController.popBackStack("dashboard", false)
                }
            )
        }
    }
}
