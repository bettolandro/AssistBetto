package com.duocuc.assistbetto.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.duocuc.assistbetto.data.UserStore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    val scrollState = rememberScrollState()

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val messageState = remember { mutableStateOf<String?>(null) }


    val acceptTermsState = remember { mutableStateOf(false) }
    val highContrastState = remember { mutableStateOf(false) }


    val textSizeOptions = listOf("Pequeño", "Medio", "Grande")
    val selectedTextSize = remember { mutableStateOf(textSizeOptions[1]) }


    val profileOptions = listOf("Estudiante", "Docente", "Invitado")
    val expanded = remember { mutableStateOf(false) }
    val selectedProfile = remember { mutableStateOf(profileOptions[0]) }


    val quickActions = listOf(
        "Lectura fácil", "Alto contraste", "Texto grande",
        "Ayuda", "Soporte", "Configuración"
    )

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
                text = "Crear cuenta",
                style = MaterialTheme.typography.titleLarge
            )


            Text(
                text = "Volver al inicio de sesión",
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

                    Text(text = "Selecciona un perfil", style = MaterialTheme.typography.titleMedium)

                    Box(modifier = Modifier.fillMaxWidth()) {

                        OutlinedTextField(
                            value = selectedProfile.value,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Perfil") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { expanded.value = !expanded.value }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Abrir menú"
                                    )
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            profileOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedProfile.value = option
                                        expanded.value = false
                                    }
                                )
                            }
                        }
                    }

                    // Inputs
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
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // Checklist (Checkbox)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = acceptTermsState.value,
                            onCheckedChange = { acceptTermsState.value = it }
                        )
                        Text("Acepto términos y condiciones")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = highContrastState.value,
                            onCheckedChange = { highContrastState.value = it }
                        )
                        Text("Activar alto contraste")
                    }

                    // Radio buttons
                    Text(text = "Tamaño de texto", style = MaterialTheme.typography.titleMedium)

                    textSizeOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTextSize.value = option }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedTextSize.value == option,
                                onClick = { selectedTextSize.value = option }
                            )
                            Text(option)
                        }
                    }


                    Button(
                        onClick = {
                            val email = emailState.value.trim()
                            val pass = passwordState.value

                            if (email.isBlank() || pass.isBlank()) {
                                messageState.value = "El correo y la contraseña son obligatorios"
                                return@Button
                            }

                            if (!acceptTermsState.value) {
                                messageState.value = "Debes aceptar los términos para registrarte."
                                return@Button
                            }

                            if (!UserStore.canAddMoreUsers()) {
                                messageState.value = "Límite alcanzado: máximo 5 usuarios."
                                return@Button
                            }

                            val ok = UserStore.addUser(email, pass)
                            messageState.value = if (ok) {
                                "Usuario registrado de manera correcta (Perfil: ${selectedProfile.value}, Texto: ${selectedTextSize.value})"
                            } else {
                                "No se pudo registrar: email duplicado o límite alcanzado."
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar")
                    }

                    messageState.value?.let { msg ->
                        Text(text = msg)
                    }

                    Text(
                        text = "Usuarios registrados: ${UserStore.getUsers().size}/5",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(8.dp))




            Spacer(Modifier.height(8.dp))


            Text(
                text = "Plantilla de accesos rápidos",
                style = MaterialTheme.typography.titleMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickActions) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}