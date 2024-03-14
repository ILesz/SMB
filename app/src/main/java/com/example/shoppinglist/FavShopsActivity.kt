package com.example.shoppinglist

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.shoppinglist.FavShopsActivity.Companion.id
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.data.items.Item
import com.example.shoppinglist.data.shops.Shop
import com.example.shoppinglist.data.shops.ShopDBViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class FavShopsActivity : ComponentActivity() {

    companion object {
        var id = 0
    }

    private lateinit var fusedLocProvider: FusedLocationProviderClient
    private lateinit var geoClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocProvider = LocationServices.getFusedLocationProviderClient(this)
        geoClient = LocationServices.getGeofencingClient(this)
        setContent {
            ShoppingListTheme {
                val dbViewModel = ShopDBViewModel(application)
                val viewModel = PreferencesViewModel.getItemViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FavShopsView(
                        dbvm = dbViewModel,
                        vm = viewModel,
                        fusedLocProvider = fusedLocProvider,
                        geoClient = geoClient
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavShopsView(
    modifier: Modifier = Modifier,
    fusedLocProvider: FusedLocationProviderClient,
    geoClient: GeofencingClient,
    dbvm: ShopDBViewModel,
    vm: PreferencesViewModel
) {
    val allShops by dbvm.shops.collectAsState(emptyMap<String, Shop>())
    var latitude by remember { mutableStateOf(0.0) }
    var longtitude by remember { mutableStateOf(0.0) }
    val context = LocalContext.current
    // var id = 0 // byl companion object???
    val c by vm.colorFlow.collectAsState(initial = "gray")
    val f by vm.fontSizeFlow.collectAsState(initial = "14")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        NavButton(
            text = "Wstecz do ekranu głównego",
            context = context,
            cls = MainActivity::class.java
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        Text("Współrzędne: szerokość=$latitude, wysokość=$longtitude .")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(
            onClick = {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context, "Brakuje zgody na lokalizację.", Toast.LENGTH_SHORT)
                        .show()
                }
                fusedLocProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener {
                        Log.i("hello", "retrieving location")
                        latitude = it.latitude
                        longtitude = it.longitude
                    }
                    .addOnFailureListener {
                        Log.e("AppError", "Brak współrzędnych.")
                    }
            },
            text = "Otrzymaj współrzędne"
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(
            onClick = {
                val geofence = Geofence.Builder()
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setRequestId("Geo${id++}")
                    .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER or
                                Geofence.GEOFENCE_TRANSITION_EXIT
                    )
                    .setCircularRegion(
                        latitude,
                        longtitude,
                        200F
                    )
                    .build()
                val georequest = GeofencingRequest.Builder()
                    .addGeofence(geofence)
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .build()
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    id,
                    Intent(context, GeoReceiver::class.java),
                    PendingIntent.FLAG_MUTABLE
                )
                geoClient.addGeofences(georequest, pendingIntent)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Geofence: $id dodany!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context, "Brak geofence [$id].", Toast.LENGTH_SHORT).show()
                    }
            },
            text = "Dodaj obszar"
        )

        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(
            onClick = {
                MapActivity.allShops = allShops.toList()
                val intent = Intent(context, MapActivity::class.java)
                context.startActivity(intent)
            },
            text = "Zobacz na mapie"
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))

        var inputTextName by remember { mutableStateOf("Mój sklep") }
        var inputTextDescription by remember { mutableStateOf("") }
        var inputTextRadius by remember { mutableStateOf("5") }

        TextLabel("nazwa")
        TextField(
            value = inputTextName,
            onValueChange = {
                inputTextName = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextLabel("opis")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextDescription,
            onValueChange = {
                inputTextDescription = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextLabel("promień")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextRadius,
            onValueChange = {
                inputTextRadius = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(onClick = {
            dbvm.insertShop(
                Shop(
                    name = inputTextName,
                    description = inputTextDescription,
                    radius = inputTextRadius.toInt(),
                    longitude = longtitude,
                    latitude = latitude
                )
            )
        }, text = "Dodaj sklep")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(allShops.toList()) { item ->
                Row() {
                    TextLabel(
                        text = "${item.second.name} %.2f E %.2f N, radius: ${item.second.radius}"
                            .format(item.second.latitude, item.second.longitude)
                    )
                    DeleteButton(onClick = {
                        dbvm.deleteShop(item.second)
                    }, text = "X")
                }
            }
        }
    }
}
