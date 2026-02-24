package com.duocuc.asistbetto.data.model

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val disabilityType: String = "",
    val communicationMode: String = "",
    val preferences: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)