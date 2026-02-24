package com.duocuc.asistbetto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duocuc.asistbetto.R
import com.duocuc.asistbetto.ui.viewmodel.DeviceViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDeviceScreen2(
    vm: DeviceViewModel,
    onBack: () -> Unit
) {
    val ping by vm.lastPing.collectAsState()
    val msg by vm.message.collectAsState()

    LaunchedEffect(Unit) { vm.loadLastPing() }

    val defaultLatLng = LatLng(-33.45, -70.66) // Santiago fallback
    val latLng = ping?.let { LatLng(it.lat, it.lng) } ?: defaultLatLng

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    LaunchedEffect(ping?.lat, ping?.lng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BuscarDispositivo (Mapa)") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.asistbettologo),
                contentDescription = "Logo AssistBetto"
            )
            Card(Modifier.fillMaxWidth()) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = true),
                    properties = MapProperties(isMyLocationEnabled = false)
                ) {
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Ubicación del dispositivo",
                        snippet = ping?.let { "Lat: ${it.lat}, Lng: ${it.lng}" } ?: "Sin datos guardados"
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { vm.updateLocation() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Actualizar ubicación") }

            Spacer(Modifier.height(12.dp))

            if (ping != null) {
                Text("Última ubicación guardada:")
                Text("Lat: ${ping!!.lat}")
                Text("Lng: ${ping!!.lng}")
            } else {
                Text("No hay ubicación guardada aún. Presiona “Actualizar ubicación”.")
            }

            if (msg != null) {
                Spacer(Modifier.height(10.dp))
                Text(msg ?: "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}