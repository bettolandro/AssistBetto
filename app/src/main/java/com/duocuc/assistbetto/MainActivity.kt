package com.duocuc.assistbetto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.duocuc.assistbetto.ui.navigation.AppNavHost
import com.duocuc.assistbetto.ui.theme.AssistBettoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssistBettoTheme {
                Scaffold(containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

