package com.duocuc.asistbetto.data.model

data class DevicePing(
    val id: String = "primary",
    val label: String = "primary",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val updatedAt: Long = System.currentTimeMillis()
)