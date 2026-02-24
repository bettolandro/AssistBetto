package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.DevicePing

interface DeviceRepository {
    suspend fun upsertDevicePing(uid: String, ping: DevicePing): Result<Unit>
    suspend fun getLastPing(uid: String, deviceId: String = "primary"): Result<DevicePing?>
}