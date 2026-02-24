package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.Phrase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestorePhraseRepository : PhraseRepository {

    private val db = Firebase.firestore

    override suspend fun addPhrase(uid: String, phrase: Phrase): Result<Unit> {
        return try {
            val ref = db.collection("users")
                .document(uid)
                .collection("phrases")
                .document()

            ref.set(phrase.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun listPhrases(uid: String, limit: Long): Result<List<Phrase>> {
        return try {
            val snap = db.collection("users")
                .document(uid)
                .collection("phrases")
                .orderBy("createdAt")
                .limitToLast(limit)
                .get()
                .await()

            Result.success(snap.toObjects(Phrase::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePhrase(uid: String, phraseId: String): Result<Unit> {
        return try {
            db.collection("users")
                .document(uid)
                .collection("phrases")
                .document(phraseId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}