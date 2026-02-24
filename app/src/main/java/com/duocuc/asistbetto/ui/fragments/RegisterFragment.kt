package com.duocuc.asistbetto.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.ui.screens.RegisterScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private val vm by lazy { AuthViewModel(FirebaseAuthRepository()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    RegisterScreen2(
                        vm = vm,
                        onBack = { findNavController().popBackStack() },
                        onRegisterSuccess = { findNavController().navigate(R.id.homeMenuFragment) }
                    )
                }
            }
        }
    }
}