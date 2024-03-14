package com.example.shoppinglist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        if (geofenceEvent != null) {
            for(geo in geofenceEvent.triggeringGeofences!!)
                if(geofenceEvent.geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER)
                    Toast.makeText(context, "Witamy w ${geo.requestId}!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "Żegnamy z ${geo.requestId}!", Toast.LENGTH_SHORT).show()
        }else{
            Log.e("GeoAppError", "geofenceEvent jest Null.")
        }
    }
}
