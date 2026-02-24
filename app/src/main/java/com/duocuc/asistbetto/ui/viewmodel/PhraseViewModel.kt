package com.duocuc.asistbetto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duocuc.asistbetto.data.model.Phrase
import com.duocuc.asistbetto.data.repository.AuthRepository
import com.duocuc.asistbetto.data.repository.PhraseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhraseViewModel(
    private val authRepo: AuthRepository,
    private val phraseRepo: PhraseRepository
) : ViewModel() {

    private val _phrases = MutableStateFlow<List<Phrase>>(emptyList())
    val phrases: StateFlow<List<Phrase>> = _phrases

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun clearMessage() { _message.value = null }


    fun load() {
        val uid = authRepo.currentUid() ?: run {
            _message.value = "Sesión inválida"
            return
        }

        viewModelScope.launch {
            phraseRepo.listPhrases(uid)
                .onSuccess { list -> _phrases.value = list.sortedByDescending { it.createdAt } }
                .onFailure { e -> _message.value = e.message ?: "Error cargando frases" }
        }
    }

    /** Normaliza texto para evitar duplicados (trim + lowercase + colapsar espacios). */
    private fun normalize(text: String): String {
        return text
            .trim()
            .lowercase()
            .replace(Regex("\\s+"), " ")
    }

    /** Obtiene el uid o lanza excepción controlada para evitar null en repos. */
    private fun requireUid(): String {
        return authRepo.currentUid() ?: throw IllegalStateException("Not logged in")
    }

    /**
     * Guarda una frase evitando duplicados por texto normalizado + type.
     * - WRITE: frase escrita
     * - SPEAK: frase dictada
     */
    fun add(text: String, type: String) {
        val clean = text.trim()
        if (clean.isBlank()) {
            _message.value = "No hay texto para guardar."
            return
        }


        if (_phrases.value.isEmpty()) {
            load()
        }

        val key = normalize(clean)

        val alreadyExists = _phrases.value.any { p ->
            normalize(p.text) == key && p.type == type
        }

        if (alreadyExists) {
            _message.value = "Esa frase ya está guardada."
            return
        }

        viewModelScope.launch {
            try {
                phraseRepo.addPhrase(
                    uid = requireUid(),
                    phrase = Phrase(text = clean, type = type)
                )
                    .onSuccess { load() } // refresca lista al guardar
                    .onFailure { _message.value = it.message ?: "Error guardando frase" }
            } catch (e: Exception) {
                _message.value = e.message ?: "Sesión inválida"
            }
        }
    }

    /** Elimina una frase por id y refresca la lista. */
    fun delete(phraseId: String) {
        if (phraseId.isBlank()) return

        viewModelScope.launch {
            try {
                phraseRepo.deletePhrase(
                    uid = requireUid(),
                    phraseId = phraseId
                )
                    .onSuccess { load() } // refresca lista al borrar
                    .onFailure { e -> _message.value = e.message ?: "Error eliminando frase" }
            } catch (e: Exception) {
                _message.value = e.message ?: "Sesión inválida"
            }
        }
    }
}