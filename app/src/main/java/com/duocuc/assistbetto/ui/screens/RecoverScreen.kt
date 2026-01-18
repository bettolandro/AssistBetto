package com.duocuc.assistbetto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.duocuc.assistbetto.data.UserStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverScreen(
    onBackToLogin: () -> Unit
) {
    val scrollState = rememberScrollState()

    val emailState = remember { mutableStateOf("") }
    val resultState = remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("AssistBetto") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Recuperar de contraseña",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Volveral inicio de sesión",
                modifier = Modifier
                    .clickable { onBackToLogin() }
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Button(
                        onClick = {
                            val email = emailState.value.trim()
                            if (email.isBlank()) {
                                resultState.value = "Debes ingresar un correo."
                            } else {
                                val pass = UserStore.findPasswordByEmail(email)
                                resultState.value = if (pass != null) {
                                    "Contraseña encontrada(por ahora la muestra, más adelante generare la funcionalidad correcta): $pass"
                                } else {
                                    "No existe un usuario con ese correo."
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Recuperar")
                    }

                    resultState.value?.let { msg ->
                        Text(msg)
                    }
                }
            }

            Text(
                text = "Nota: esta recuperación es demostrativa y no reemplaza el proceso de recuperación real.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}