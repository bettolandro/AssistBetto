package com.duocuc.asistbetto.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duocuc.asistbetto.data.UserRepository
import com.duocuc.asistbetto.navigation.Routes
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController) {


    val context = LocalContext.current


    val users = UserRepository.users


    var messageToSpeak by remember { mutableStateOf("") }


    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val ttsState = remember { mutableStateOf<TextToSpeech?>(null) }


    DisposableEffect(Unit) {


        var localTts: TextToSpeech? = null

        localTts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {

                localTts?.language = Locale("es", "ES")
            }
        }


        ttsState.value = localTts

        onDispose {
            ttsState.value?.stop()
            ttsState.value?.shutdown()
            ttsState.value = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "AsistBetto",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Escribe y reproduce en voz para comunicarte en tu entorno cotidiano.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Escribir y hablar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = messageToSpeak,
                    onValueChange = { messageToSpeak = it },
                    label = { Text("Escribe aquí tu mensaje") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val text = messageToSpeak.trim()

                        if (text.isEmpty()) {
                            scope.launch {
                                snackState.showSnackbar("Escribe un mensaje antes de reproducir.")
                            }
                            return@Button
                        }

                        val tts = ttsState.value
                        if (tts == null) {
                            scope.launch {
                                snackState.showSnackbar("TTS no está listo aún. Intenta otra vez.")
                            }
                            return@Button
                        }

                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ASISTBETTO_TTS")

                        scope.launch {
                            snackState.showSnackbar("Reproduciendo mensaje en voz.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hablar (reproducir en voz)")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Frases rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { messageToSpeak = "Hola, ¿me puedes ayudar por favor?" },
                        modifier = Modifier.weight(1f)
                    ) { Text("Ayuda") }

                    TextButton(
                        onClick = { messageToSpeak = "Gracias." },
                        modifier = Modifier.weight(1f)
                    ) { Text("Gracias") }

                    TextButton(
                        onClick = { messageToSpeak = "Necesito comunicarme, por favor ten paciencia." },
                        modifier = Modifier.weight(1f)
                    ) { Text("Paciencia") }
                }
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(10.dp))
        SnackbarHost(hostState = snackState)
    }
}