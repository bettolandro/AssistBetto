package com.duocuc.asistbetto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.data.repository.FirestorePhraseRepository
import com.duocuc.asistbetto.ui.screens.SpeakScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.PhraseViewModel
import com.duocuc.asistbetto.ui.viewmodel.PhraseViewModelFactory

class SpeakFragment : Fragment() {

    private lateinit var vm: PhraseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(
            this,
            PhraseViewModelFactory(
                authRepo = FirebaseAuthRepository(),
                phraseRepo = FirestorePhraseRepository()
            )
        )[PhraseViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()

        vm.load()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    SpeakScreen2(
                        vm = vm,
                        onBack = { findNavController().popBackStack() }
                    )
                }
            }
        }
    }
}