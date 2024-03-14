package com.example.shoppinglist.data.items

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

//class ItemRepo(private val itemDao: ItemDAO) {
class ItemRepo(private val firebaseDatabase: FirebaseDatabase){
    val allItems = MutableStateFlow(HashMap<String, Item>())
    private val items: String = "items"

    init{
        firebaseDatabase.getReference(items).addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = Item(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    number = (snapshot.child("number").value as Long).toInt(),
                    price = (snapshot.child("price").value as Long).toFloat(),
                    bought = snapshot.child("bought").value as Boolean
                )
                allItems.value = allItems.value.toMutableMap().apply{
                    put(item.id, item)
                } as HashMap<String, Item>
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = Item(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    number = (snapshot.child("number").value as Long).toInt(),
                    price = (snapshot.child("price").value as Long).toFloat(),
                    bought = snapshot.child("bought").value as Boolean
                )
                allItems.value = allItems.value.toMutableMap().apply{
                    put(item.id, item)
                } as HashMap<String, Item>
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = Item(
                    id = snapshot.ref.key as String,
                    name = snapshot.child("name").value as String,
                    number = (snapshot.child("number").value as Long).toInt(),
                    price = (snapshot.child("price").value as Long).toFloat(),
                    bought = snapshot.child("bought").value as Boolean
                )
                allItems.value = allItems.value.toMutableMap().apply{
                    remove(item.id, item)
                } as HashMap<String, Item>
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", error.message)
            }
        })
    }

    suspend fun insert(item: Item) {
        firebaseDatabase.getReference(items).push().also{
            item.id = it.ref.key.toString()
            it.setValue(item)
        }
    }
    suspend fun update(item: Item) {
        firebaseDatabase.getReference(items+"/${item.id}").child("name").setValue(item.name)
        firebaseDatabase.getReference(items+"/${item.id}").child("number").setValue(item.number)
        firebaseDatabase.getReference(items+"/${item.id}").child("price").setValue(item.price)
        firebaseDatabase.getReference(items+"/${item.id}").child("bought").setValue(item.bought)
        //number, price, bought
    }
    suspend fun delete(item: Item) {
        firebaseDatabase.getReference(items+"/${item.id}").removeValue()
    }


    /*
    val allItems = itemDao.getItems()

    suspend fun insert(item: Item) = itemDao.insertItem(item)
    suspend fun update(item: Item) = itemDao.updateItem(item)
    suspend fun delete(item: Item) = itemDao.deleteItem(item)
    */


}