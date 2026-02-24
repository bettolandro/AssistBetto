package com.duocuc.asistbetto.data.repository

import com.duocuc.asistbetto.data.model.Phrase

interface PhraseRepository {
    suspend fun addPhrase(uid: String, phrase: Phrase): Result<Unit>
    suspend fun listPhrases(uid: String, limit: Long = 30): Result<List<Phrase>>
    suspend fun deletePhrase(uid: String, phraseId: String): Result<Unit>
}