package com.duocuc.asistbetto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.asistbetto.data.model.DevicePing
import com.duocuc.asistbetto.data.repository.AuthRepository
import com.duocuc.asistbetto.data.repository.DeviceRepository
import com.duocuc.asistbetto.services.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val authRepo: AuthRepository,
    private val deviceRepo: DeviceRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _lastPing = MutableStateFlow<DevicePing?>(null)
    val lastPing: StateFlow<DevicePing?> = _lastPing

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun clearMessage() { _message.value = null }

    fun loadLastPing() {
        val uid = authRepo.currentUid() ?: run {
            _message.value = "Sesión inválida"
            return
        }

        viewModelScope.launch {
            deviceRepo.getLastPing(uid, "primary")
                .onSuccess { _lastPing.value = it }
                .onFailure { _message.value = it.message ?: "Error cargando ubicación" }
        }
    }

    fun updateLocation() {
        val uid = authRepo.currentUid() ?: run {
            _message.value = "Sesión inválida"
            return
        }

        viewModelScope.launch {
            val loc = try { locationService.getLastLocation() } catch (_: Exception) { null }

            if (loc == null) {
                _message.value = "No se pudo obtener ubicación (activa GPS y permisos)"
                return@launch
            }

            val ping = DevicePing(
                id = "primary",
                label = "primary",
                lat = loc.latitude,
                lng = loc.longitude,
                updatedAt = System.currentTimeMillis()
            )

            deviceRepo.upsertDevicePing(uid, ping)
                .onSuccess {
                    _lastPing.value = ping
                    _message.value = "Ubicación actualizada"
                }
                .onFailure { _message.value = it.message ?: "Error guardando ubicación" }
        }
    }
}