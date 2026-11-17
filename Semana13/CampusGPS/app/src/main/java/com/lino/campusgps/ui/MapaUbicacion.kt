package com.lino.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.lino.campusgps.model.LugarConDistancia
import com.lino.campusgps.model.UbicacionUi
import androidx.compose.runtime.key
import com.google.android.gms.maps.model.BitmapDescriptorFactory
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    16f
                )
            )
        }
    }
    LaunchedEffect(seleccionado) {
        val l = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(LatLng(l.latitud, l.longitud), 17f)
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }

        lugares.forEach { item ->
            val l = item.lugar
            key(l.id) {
                val ms = rememberUpdatedMarkerState(position = LatLng(l.latitud, l.longitud))
                LaunchedEffect(seleccionado) { if (seleccionado == l.id) ms.showInfoWindow() }
                Marker(
                    state = ms,
                    title = l.nombre,
                    snippet = l.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
    }
}
