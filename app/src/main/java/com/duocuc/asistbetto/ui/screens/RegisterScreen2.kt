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
import com.duocuc.asistbetto.data.model.UserProfile
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen2(
    vm: AuthViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val msg by vm.message.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var disabilityType by remember { mutableStateOf("") }
    var communicationMode by remember { mutableStateOf("") }

    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }
    var show2 by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Registro") }) }) { inner ->
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
            OutlinedTextField(fullName, { fullName = it }, Modifier.fillMaxWidth(), label = { Text("Nombre completo") })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(username, { username = it }, Modifier.fillMaxWidth(), label = { Text("Usuario") })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(disabilityType, { disabilityType = it }, Modifier.fillMaxWidth(), label = { Text("Tipo discapacidad") })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(communicationMode, { communicationMode = it }, Modifier.fillMaxWidth(), label = { Text("Modo comunicación") })
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                pass, { pass = it }, Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { TextButton(onClick = { show = !show }) { Text(if (show) "Ocultar" else "Mostrar") } }
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                pass2, { pass2 = it }, Modifier.fillMaxWidth(),
                label = { Text("Repetir contraseña") },
                isError = pass2.isNotBlank() && pass2 != pass,
                visualTransformation = if (show2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { TextButton(onClick = { show2 = !show2 }) { Text(if (show2) "Ocultar" else "Mostrar") } }
            )

            if (pass2.isNotBlank() && pass2 != pass) {
                Spacer(Modifier.height(6.dp))
                Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (pass != pass2) return@Button
                    vm.register(
                        profile = UserProfile(
                            fullName = fullName.trim(),
                            username = username.trim(),
                            email = email.trim(),
                            disabilityType = disabilityType.trim(),
                            communicationMode = communicationMode.trim()
                        ),
                        password = pass,
                        onSuccess = onRegisterSuccess
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) { Text(if (loading) "Registrando..." else "Registrar") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }

            if (msg != null) {
                Spacer(Modifier.height(10.dp))
                Text(msg ?: "", color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}