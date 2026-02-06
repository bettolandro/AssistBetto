package com.duocuc.asistbetto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duocuc.asistbetto.navigation.Routes
import com.duocuc.asistbetto.ui.screens.HomeScreen
import com.duocuc.asistbetto.ui.screens.LoginScreen
import com.duocuc.asistbetto.ui.screens.RecoverPasswordScreen
import com.duocuc.asistbetto.ui.screens.RegisterScreen
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsistBettoTheme {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(navController = navController)
            }
            composable(Routes.REGISTER) {
                RegisterScreen(navController = navController)
            }
            composable(Routes.RECOVER) {
                RecoverPasswordScreen(navController = navController)
            }
            composable(Routes.HOME) {
                HomeScreen(navController = navController)
            }
        }
    }
}