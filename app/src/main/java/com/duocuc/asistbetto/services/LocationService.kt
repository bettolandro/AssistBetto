package com.duocuc.asistbetto.services

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationService(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): android.location.Location? {
        val client = LocationServices.getFusedLocationProviderClient(context)
        return client.lastLocation.await()
    }
}