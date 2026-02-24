package com.duocuc.asistbetto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen2(
    vm: AuthViewModel,
    onGoRegister: () -> Unit,
    onGoRecover: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val msg by vm.message.collectAsState()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Iniciar Sesión") }) }) { inner ->
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { show = !show }) { Text(if (show) "Ocultar" else "Mostrar") }
                }
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { vm.login(email, pass, onLoginSuccess) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) { Text(if (loading) "Ingresando..." else "Ingresar") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta")
            }
            TextButton(onClick = onGoRecover, modifier = Modifier.fillMaxWidth()) {
                Text("Recuperar contraseña")
            }

            if (msg != null) {
                Spacer(Modifier.height(10.dp))
                Text(msg ?: "", color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}