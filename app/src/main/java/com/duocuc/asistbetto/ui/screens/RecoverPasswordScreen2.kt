package com.duocuc.asistbetto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen2(
    vm: AuthViewModel,
    onBack: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val msg by vm.message.collectAsState()
    var email by remember { mutableStateOf("") }
    var done by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Recuperar contraseña") }) }) { inner ->
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
            Text("Ingresa tu email y te enviaremos un enlace para restablecer tu contraseña.")
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") }, singleLine = true)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { vm.recoverPassword(email) { done = true } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) { Text(if (loading) "Enviando..." else "Enviar") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }

            if (done) {
                Spacer(Modifier.height(10.dp))
                Text("Si el correo existe, recibirás el enlace en unos minutos.")
            }

            if (msg != null) {
                Spacer(Modifier.height(10.dp))
                Text(msg ?: "", color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}