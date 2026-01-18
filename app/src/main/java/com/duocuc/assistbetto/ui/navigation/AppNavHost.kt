package com.duocuc.assistbetto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duocuc.assistbetto.ui.screens.HomeScreen
import com.duocuc.assistbetto.ui.screens.LoginScreen
import com.duocuc.assistbetto.ui.screens.RecoverScreen
import com.duocuc.assistbetto.ui.screens.RegisterScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoRecover = { navController.navigate(Routes.RECOVER) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(onBackToLogin = { navController.popBackStack() })
        }

        composable(Routes.RECOVER) {
            RecoverScreen(onBackToLogin = { navController.popBackStack() })
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}