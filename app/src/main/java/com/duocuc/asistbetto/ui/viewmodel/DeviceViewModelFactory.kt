package com.duocuc.asistbetto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duocuc.asistbetto.data.repository.AuthRepository
import com.duocuc.asistbetto.data.repository.DeviceRepository
import com.duocuc.asistbetto.services.LocationService

class DeviceViewModelFactory(
    private val authRepo: AuthRepository,
    private val deviceRepo: DeviceRepository,
    private val locationService: LocationService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceViewModel(authRepo, deviceRepo, locationService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}