package com.duocuc.asistbetto.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.ui.screens.RecoverPasswordScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

class RecoverPasswordFragment : Fragment() {

    private val vm by lazy { AuthViewModel(FirebaseAuthRepository()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    RecoverPasswordScreen2(
                        vm = vm,
                        onBack = { findNavController().popBackStack() }
                    )
                }
            }
        }
    }
}