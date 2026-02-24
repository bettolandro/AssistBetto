package com.duocuc.asistbetto.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.ui.viewmodel.PhraseViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WriteScreen2(
    vm: PhraseViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    var messageToSpeak by remember { mutableStateOf("") }
    val phrases by vm.phrases.collectAsState()

    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Respaldo de refresco al entrar
    LaunchedEffect(Unit) { vm.load() }

    // TTS estable
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
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.asistbettologo),
            contentDescription = "Logo AsistBetto",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Text(
            text = "Escribir",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Escribe un mensaje y reprodúcelo en voz para comunicarte.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Mensaje",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = messageToSpeak,
                    onValueChange = { messageToSpeak = it },
                    label = { Text("Escribe aquí tu mensaje") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val text = messageToSpeak.trim()

                        if (text.isEmpty()) {
                            scope.launch { snackState.showSnackbar("Escribe un mensaje antes de reproducir.") }
                            return@Button
                        }

                        val tts = ttsState.value
                        if (tts == null) {
                            scope.launch { snackState.showSnackbar("Motor de voz no listo. Revisa 'Texto a voz' en Ajustes.") }
                            return@Button
                        }

                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ASISTBETTO_TTS")
                        scope.launch { snackState.showSnackbar("Reproduciendo mensaje en voz.") }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hablar (reproducir en voz)")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        val text = messageToSpeak.trim()
                        if (text.isEmpty()) {
                            scope.launch { snackState.showSnackbar("No hay texto para guardar.") }
                            return@OutlinedButton
                        }

                        // evita duplicados y refresca al guardar
                        vm.add(text = text, type = "WRITE")
                        scope.launch { snackState.showSnackbar("Procesando guardado…") }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar frase")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Frases guardadas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (phrases.isEmpty()) {
            Text(
                text = "Aún no hay frases guardadas.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                phrases.take(10).forEach { p ->

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        messageToSpeak = p.text
                                        scope.launch { snackState.showSnackbar("Frase seleccionada") }
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = p.text, maxLines = 2)
                                    Text(text = p.type, style = MaterialTheme.typography.bodySmall)
                                }

                                IconButton(
                                    onClick = {
                                        vm.delete(p.id)
                                        scope.launch { snackState.showSnackbar("Eliminando…") }
                                    }
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar frase")
                                }
                            }
                        }


                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(12.dp))
        SnackbarHost(hostState = snackState)
    }
}