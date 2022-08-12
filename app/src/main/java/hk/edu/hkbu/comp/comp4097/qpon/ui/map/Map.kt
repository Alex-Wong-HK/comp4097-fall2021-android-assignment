package hk.edu.hkbu.comp.comp4097.qpon.ui.map

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import json.MallViewModel

data class Building(val title:String, val latitude:Double, val longitude:Double) {
    companion object {
        val data = listOf(
            Building("IFC Mall", 22.2849,114.158917),
            Building("Pacific Place", 22.2774985, 114.1663225),
            Building("Times Square", 22.2782079, 114.1822994),
            Building("Elements", 22.3048708, 114.1615219),
            Building("Harbour City", 22.2950689, 114.1668661),
            Building("Festival Walk", 22.3372971, 114.1745273),
            Building("MegaBox", 22.319857, 114.208168),
            Building("APM", 22.3121738, 114.22513219999996),
            Building("Tsuen Wan Plaza", 22.370735, 114.111309),
            Building("New Town Plaza", 22.3814592, 114.1889307),
        )
    }
}

@Composable
fun Map(nav: NavHostController, mall: String ="") {
    var latitude = Building.data.filter { it.title == mall }.single().latitude
    var longitude = Building.data.filter { it.title == mall }.single().longitude
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurant") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        // handler for physical back button
                        BackHandler(enabled = true, onBack = { nav.popBackStack() })
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){
        AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { map ->
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(latitude, longitude), 15f
                        )
                    )
                    Building.data.forEach { building ->
                        map.addMarker(
                            MarkerOptions().position(
                                LatLng(building.latitude, building.longitude)
                            ).title(building.title)
                        )
                    }
                }
            }
        },
        update = { view -> view.onResume() }
    )}
}