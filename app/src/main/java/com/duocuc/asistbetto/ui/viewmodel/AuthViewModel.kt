package com.duocuc.asistbetto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.asistbetto.data.model.UserProfile
import com.duocuc.asistbetto.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun clearMessage() { _message.value = null }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val res = repo.login(email, password)
            _loading.value = false

            res.onSuccess { onSuccess() }
                .onFailure { _message.value = it.message ?: "Error al iniciar sesión" }
        }
    }

    fun register(profile: UserProfile, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val res = repo.register(profile, password)
            _loading.value = false

            res.onSuccess { onSuccess() }
                .onFailure { _message.value = it.message ?: "Error al registrar" }
        }
    }

    fun recoverPassword(email: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val res = repo.recoverPassword(email)
            _loading.value = false

            res.onSuccess { onSuccess() }
                .onFailure { _message.value = it.message ?: "Error al enviar correo" }
        }
    }

    fun logout() = repo.logout()
}