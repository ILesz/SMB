package com.example.shoppinglist

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.shoppinglist.data.shops.Shop
import com.example.shoppinglist.databinding.ActivityMapBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class MapActivity : ComponentActivity() {

    private lateinit var permissionsManager: PermissionsManager
    private lateinit var binding: ActivityMapBinding
    private lateinit var locationManager: LocationManager

    companion object{
        var allShops: List<Pair<String, Shop>> = emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this@MapActivity
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.also {
            it.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS
            )
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(context, FavShopsActivity::class.java)
            context.startActivity(intent)
        }

        var permissionsListener: PermissionsListener = object : PermissionsListener {
            override fun onExplanationNeeded(permissionsToExplain: List<String>) {}

            override fun onPermissionResult(granted: Boolean) {
                if (granted) {
                    // Permission sensitive logic called here,
                    // such as activating the Maps SDK's LocationComponent to show the device's location
                } else {
                    // User denied the permission
                }
            }
        }

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Permission sensitive logic called here,
            // such as activating the Maps SDK's LocationComponent to show the device's location
        } else {
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(this)
        }

        for (item in allShops) {
            Log.i("hello from loop", "hello from loop")
            addAnnotationToMap(item.second.name, item.second.longitude, item.second.latitude)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addAnnotationToMap(title: String, longitude: Double, latitude: Double) {
        val pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        val marker = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val scaledMarker = Bitmap.createScaledBitmap(
            marker,
            (marker.width * 0.3).toInt(),
            (marker.height * 0.3).toInt(),
            true
        )

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val paOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(longitude, latitude))
            .withIconImage(scaledMarker)
            .withTextAnchor(TextAnchor.TOP)
            .withTextField(title)

        pointAnnotationManager.create(paOptions)
    }
}