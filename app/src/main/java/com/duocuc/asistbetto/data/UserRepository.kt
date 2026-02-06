package com.duocuc.asistbetto.data

import androidx.compose.runtime.mutableStateListOf

object UserRepository {

    // Lista observable para Compose.
    private val _users = mutableStateListOf<User>()

    // Vista de solo lectura.
    val users: List<User> get() = _users

    // Agrega usuario si no supera 5 y no se repite.
    fun addUser(user: User): Boolean {
        if (_users.size >= 5) return false

        val exists = _users.any { it.username.equals(user.username, ignoreCase = true) }
        if (exists) return false

        _users.add(user)
        return true
    }

    // Valida login por username + password.
    fun validateLogin(username: String, password: String): Boolean {
        return _users.any {
            it.username.equals(username, ignoreCase = true) && it.password == password
        }
    }

    // Verifica si existe username (para recuperar contraseña en demo).
    fun userExists(username: String): Boolean {
        return _users.any { it.username.equals(username, ignoreCase = true) }
    }
}