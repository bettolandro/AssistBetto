package com.duocuc.asistbetto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duocuc.asistbetto.data.repository.AuthRepository
import com.duocuc.asistbetto.data.repository.PhraseRepository

class PhraseViewModelFactory(
    private val authRepo: AuthRepository,
    private val phraseRepo: PhraseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhraseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhraseViewModel(authRepo, phraseRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}