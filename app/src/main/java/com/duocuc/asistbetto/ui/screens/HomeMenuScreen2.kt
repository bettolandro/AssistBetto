package com.duocuc.asistbetto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duocuc.asistbetto.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenuScreen2(
    onWrite: () -> Unit,
    onSpeak: () -> Unit,
    onSearchDevice: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("AssistBetto - Menú") }) }
    ) { inner ->
        ScrollableScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.asistbettologo),
                contentDescription = "Logo AssistBetto"
            )

            Text("Selecciona una opción", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))

            Button(onClick = onWrite, modifier = Modifier.fillMaxWidth()) {
                Text("✍️ Escribir")
            }
            Spacer(Modifier.height(12.dp))

            Button(onClick = onSpeak, modifier = Modifier.fillMaxWidth()) {
                Text("🗣️ Hablar")
            }
            Spacer(Modifier.height(12.dp))

            Button(onClick = onSearchDevice, modifier = Modifier.fillMaxWidth()) {
                Text("📍 Buscar Dispositivo (Mapa)")
            }
            Spacer(Modifier.height(24.dp))

            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}