package com.duocuc.asistbetto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.data.UserRepository
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreen(navController: NavController) {


    var username by remember { mutableStateOf("") }


    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.asistbettologo),
            contentDescription = "Logo AssistBetto"
        )

        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(8.dp)) {


                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))


                Button(
                    onClick = {
                        val user = username.trim()

                        if (user.isEmpty()) {
                            scope.launch {
                                snackState.showSnackbar("Debes completar el campo solicitado para poder recuperar la clave")
                            }
                            return@Button
                        }

                        val exists = UserRepository.userExists(user)

                        scope.launch {
                            if (exists) {
                                //no existe proceso de correo, es solo una idea antes de la app final
                                snackState.showSnackbar(
                                    "Usuario encontrado. Se ha enviado un correo con las instrucciones."
                                )
                            } else {
                                snackState.showSnackbar("No existe ese usuario. Verifica el dato ingresado.")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Recuperar")
                }

                Spacer(modifier = Modifier.height(8.dp))


                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SnackbarHost(hostState = snackState)
    }
}