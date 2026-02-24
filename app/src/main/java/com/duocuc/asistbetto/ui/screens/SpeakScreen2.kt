package com.duocuc.asistbetto.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.ui.viewmodel.PhraseViewModel
import kotlinx.coroutines.launch

@Composable
fun SpeakScreen2(
    vm: PhraseViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    var recognizedText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    val phrases by vm.phrases.collectAsState()

    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Respaldo de refresco al entrar
    LaunchedEffect(Unit) { vm.load() }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        message = if (granted) "Permiso concedido. Ahora puedes hablar." else "Permiso de micrófono rechazado"
    }

    val sttLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            recognizedText = matches?.firstOrNull().orEmpty()
        } else {
            message = "No se obtuvo audio o se canceló el reconocimiento."
        }
    }

    fun startSpeechToText() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora…")
        }

        sttLauncher.launch(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.asistbettologo),
            contentDescription = "Logo AssistBetto",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Text("Hablar", style = MaterialTheme.typography.headlineSmall)

        if (message != null) {
            Card {
                Text(
                    text = message!!,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        OutlinedTextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Texto reconocido (puedes editarlo)") },
            minLines = 4
        )

        Button(
            onClick = { startSpeechToText() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar reconocimiento")
        }

        OutlinedButton(
            onClick = {
                val clean = recognizedText.trim()
                if (clean.isBlank()) {
                    message = "No hay texto para guardar."
                } else {
                    //evita duplicados y refresca al guardar
                    vm.add(text = clean, type = "SPEAK")
                    scope.launch { snackState.showSnackbar("Procesando guardado…") }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar frase")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Frases guardadas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

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
                                        recognizedText = p.text
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

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        SnackbarHost(hostState = snackState)
    }
}