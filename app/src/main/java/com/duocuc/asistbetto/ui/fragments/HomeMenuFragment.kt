package com.duocuc.asistbetto.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.ui.screens.HomeMenuScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModel
import com.duocuc.asistbetto.ui.viewmodel.AuthViewModelFactory

class HomeMenuFragment : Fragment() {

    private lateinit var authVm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authVm = ViewModelProvider(this, AuthViewModelFactory(FirebaseAuthRepository()))[AuthViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    HomeMenuScreen2(
                        onWrite = { findNavController().navigate(R.id.writeFragment) },
                        onSpeak = { findNavController().navigate(R.id.speakFragment) },
                        onSearchDevice = { findNavController().navigate(R.id.searchDeviceFragment) },
                        onLogout = {
                            authVm.logout()
                            findNavController().navigate(R.id.loginFragment)
                        }
                    )
                }
            }
        }
    }
}