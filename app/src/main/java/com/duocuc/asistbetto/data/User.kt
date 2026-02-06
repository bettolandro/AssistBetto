package com.duocuc.asistbetto.data

data class User(
    val username: String,          // Usuario (nickname o correo)
    val password: String,          // Contraseña
    val fullName: String,          // Nombre completo
    val disabilityType: String,    // ComboBox (dropdown)
    val communicationMode: String, // RadioButtons
    val preferences: List<String>  // Checklist (checkboxes)
)