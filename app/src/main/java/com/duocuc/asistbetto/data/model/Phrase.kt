package com.duocuc.asistbetto.data.model

data class Phrase(
    val id: String = "",
    val text: String = "",
    val type: String = "WRITE", // WRITE | SPEAK
    val createdAt: Long = System.currentTimeMillis()
)