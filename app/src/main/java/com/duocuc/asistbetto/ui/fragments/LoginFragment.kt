package com.duocuc.asistbetto.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.ui.screens.LoginScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private val vm by lazy { AuthViewModel(FirebaseAuthRepository()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    LoginScreen2(
                        vm = vm,
                        onGoRegister = { findNavController().navigate(R.id.registerFragment) },
                        onGoRecover = { findNavController().navigate(R.id.recoverPasswordFragment) },
                        onLoginSuccess = { findNavController().navigate(R.id.homeMenuFragment) }
                    )
                }
            }
        }
    }
}