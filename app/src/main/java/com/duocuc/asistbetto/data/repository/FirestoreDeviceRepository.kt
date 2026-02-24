package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.DevicePing
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestoreDeviceRepository : DeviceRepository {

    private val db = Firebase.firestore

    override suspend fun upsertDevicePing(uid: String, ping: DevicePing): Result<Unit> {
        return try {
            val id = ping.id.ifBlank { "primary" }

            db.collection("users")
                .document(uid)
                .collection("devices")
                .document(id)
                .set(ping.copy(id = id, label = id))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLastPing(uid: String, deviceId: String): Result<DevicePing?> {
        return try {
            val doc = db.collection("users")
                .document(uid)
                .collection("devices")
                .document(deviceId)
                .get()
                .await()

            Result.success(doc.toObject(DevicePing::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}