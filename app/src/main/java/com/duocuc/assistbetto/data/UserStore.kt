package com.duocuc.assistbetto.data

import androidx.compose.runtime.mutableStateListOf

object UserStore {

    private val users = mutableStateListOf<User>()

    fun getUsers(): List<User> = users

    fun canAddMoreUsers(): Boolean = users.size < 5

    fun addUser(email: String, password: String): Boolean {
        if (!canAddMoreUsers()) return false

        val normalizedEmail = email.trim()
        val exists = users.any { it.email.equals(normalizedEmail, ignoreCase = true) }
        if (exists) return false

        users.add(User(email = normalizedEmail, password = password))
        return true
    }

    fun validateLogin(email: String, password: String): Boolean {
        val normalizedEmail = email.trim()
        return users.any {
            it.email.equals(normalizedEmail, ignoreCase = true) && it.password == password
        }
    }

    fun findPasswordByEmail(email: String): String? {
        val normalizedEmail = email.trim()
        return users.firstOrNull { it.email.equals(normalizedEmail, ignoreCase = true) }?.password
    }
}