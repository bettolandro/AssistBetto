package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.UserProfile

interface AuthRepository {
    suspend fun register(profile: UserProfile, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun recoverPassword(email: String): Result<Unit>
    fun currentUid(): String?
    fun logout()
}