package com.duocuc.assistbetto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.duocuc.assistbetto.data.UserStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onGoRecover: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val scrollState = rememberScrollState()

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val messageState = remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AssistBetto") }
            )
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
                text = "Inicio de Sesión",
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
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

                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Button(
                        onClick = {
                            val email = emailState.value.trim()
                            val pass = passwordState.value

                            if (email.isBlank() || pass.isBlank()) {
                                messageState.value = "El correo y la contraseña son obligatorios."
                                return@Button
                            } else {
                                val ok = UserStore.validateLogin(email, pass)
                                if (ok) {
                                    messageState.value = "Inicio de sesión exitoso"
                                    onLoginSuccess()
                                } else {
                                    messageState.value = "Usuario y/o Contraseña incorrecta"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ingresar")
                    }

                    messageState.value?.let { Text(it) }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                modifier = Modifier
                    .clickable { onGoRegister() }
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "¿Olvidaste tu contraseña? Recuperala aqui",
                modifier = Modifier
                    .clickable { onGoRecover() }
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.primary
            )


        }
    }
}