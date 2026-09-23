package com.lino.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lino.navlab.screens.DetailScreen
import com.lino.navlab.screens.HomeScreen
import com.lino.navlab.screens.ListScreen
import com.lino.navlab.screens.LoginScreen
import com.lino.navlab.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    defaultValue = "Mijael Lino"
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Mijael Lino"
            HomeScreen(navController, userName)
        }

        composable(Screen.List.route) {
            ListScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            DetailScreen(navController, itemId)
        }
    }
}
