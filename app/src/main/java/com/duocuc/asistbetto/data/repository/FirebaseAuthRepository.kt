package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository : AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    override suspend fun register(profile: UserProfile, password: String): Result<Unit> {
        return try {
            val res = auth.createUserWithEmailAndPassword(profile.email.trim(), password).await()
            val uid = res.user?.uid ?: return Result.failure(IllegalStateException("UID nulo"))

            val doc = profile.copy(uid = uid, email = profile.email.trim())

            db.collection("users")
                .document(uid)
                .set(doc)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email.trim(), password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun currentUid(): String? = auth.currentUser?.uid

    override fun logout() {
        auth.signOut()
    }
}