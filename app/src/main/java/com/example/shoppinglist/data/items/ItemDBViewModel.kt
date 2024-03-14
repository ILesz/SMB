package com.example.shoppinglist.data.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemDBViewModel(private val application: Application) : AndroidViewModel(application) {
    private val itemRepo: ItemRepo
    private val firebaseDB: FirebaseDatabase
    val items: StateFlow<HashMap<String, Item>>
    private val DBref: DatabaseReference

    init {
        //val itemDao = DB.getDatabase(application).itemDAO()
        //itemRepo = ItemRepo(itemDao)
        firebaseDB = FirebaseDatabase.getInstance("https://shoppinglist-2584b-default-rtdb.europe-west1.firebasedatabase.app/")
        DBref = firebaseDB.getReference()
        itemRepo = ItemRepo(firebaseDB)
        items = itemRepo.allItems
    }

    fun insertItem(item: Item){
        viewModelScope.launch {
            itemRepo.insert(item)
        }
    }

    fun updateItem(item: Item){
        viewModelScope.launch {
            itemRepo.update(item)
        }
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            itemRepo.delete(item)
        }
    }
}