package com.example.shoppinglist.data.shops

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class ShopRepo(private val firebaseDatabase: FirebaseDatabase) {

    //val allshops: StateFlow<HashMap<String, Shop>>

    val allShops = MutableStateFlow(HashMap<String, Shop>())
    private val shops: String = "shops"

    init{
        firebaseDatabase.getReference(shops).addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val shop  = Shop(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    description = snapshot.child("description").value as String,
                    radius = (snapshot.child("radius").value as Long).toInt(),
                    longitude = snapshot.child("longitude").value as Double,
                    latitude = snapshot.child("latitude").value as Double
                )
                allShops.value = allShops.value.toMutableMap().apply {
                    put(shop.id, shop)
                } as HashMap<String, Shop>
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val shop  = Shop(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    description = snapshot.child("description").value as String,
                    radius = (snapshot.child("radius").value as Long).toInt(),
                    longitude = snapshot.child("longitude").value as Double,
                    latitude = snapshot.child("latitude").value as Double
                )
                allShops.value = allShops.value.toMutableMap().apply {
                    put(shop.id, shop)
                } as HashMap<String, Shop>
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val shop  = Shop(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    description = snapshot.child("description").value as String,
                    radius = (snapshot.child("radius").value as Long).toInt(),
                    longitude = snapshot.child("longitude").value as Double,
                    latitude = snapshot.child("latitude").value as Double
                )
                allShops.value = allShops.value.toMutableMap().apply {
                    remove(shop.id, shop)
                } as HashMap<String, Shop>
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", error.message)
            }

        })
    }

    suspend fun insert(shop: Shop) {
        firebaseDatabase.getReference(shops).push().also{
            shop.id = it.ref.key.toString()
            it.setValue(shop)
        }
    }
    suspend fun update(shop: Shop) {
        firebaseDatabase.getReference(shops+"/${shop.id}").child("name").setValue(shop.name)
        firebaseDatabase.getReference(shops+"/${shop.id}").child("description").setValue(shop.description)
        firebaseDatabase.getReference(shops+"/${shop.id}").child("radius").setValue(shop.radius)
        firebaseDatabase.getReference(shops+"/${shop.id}").child("longitude").setValue(shop.longitude)
        firebaseDatabase.getReference(shops+"/${shop.id}").child("latitude").setValue(shop.latitude)
    }
    suspend fun delete(shop: Shop) {
        firebaseDatabase.getReference(shops+"/${shop.id}").removeValue()
    }
}
