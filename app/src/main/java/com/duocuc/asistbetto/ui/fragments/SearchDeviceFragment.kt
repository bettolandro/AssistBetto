package com.duocuc.asistbetto.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.duocuc.asistbetto.data.repository.FirebaseAuthRepository
import com.duocuc.asistbetto.data.repository.FirestoreDeviceRepository
import com.duocuc.asistbetto.services.LocationService
import com.duocuc.asistbetto.ui.screens.SearchDeviceScreen2
import com.duocuc.asistbetto.ui.theme.AsistBettoTheme
import com.duocuc.asistbetto.ui.viewmodel.DeviceViewModel
import com.duocuc.asistbetto.ui.viewmodel.DeviceViewModelFactory

class SearchDeviceFragment : Fragment() {

    private lateinit var vm: DeviceViewModel

    private val requestLocationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) vm.updateLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(
            this,
            DeviceViewModelFactory(
                authRepo = FirebaseAuthRepository(),
                deviceRepo = FirestoreDeviceRepository(),
                locationService = LocationService(requireContext())
            )
        )[DeviceViewModel::class.java]

        requestLocationPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AsistBettoTheme {
                    SearchDeviceScreen2(
                        vm = vm,
                        onBack = { findNavController().popBackStack() }
                    )
                }
            }
        }
    }
}