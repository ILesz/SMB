package com.example.shoppinglist.data.shops

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShopDBViewModel(private val application: Application) : AndroidViewModel(application) {
    private val shopRepo: ShopRepo
    private val firebaseDB: FirebaseDatabase
    val shops: StateFlow<HashMap<String, Shop>>
    
    init {
        firebaseDB = FirebaseDatabase.getInstance("https://shoppinglist-2584b-default-rtdb.europe-west1.firebasedatabase.app/")
        shopRepo = ShopRepo(firebaseDB)
        shops = shopRepo.allShops
    }
    
    fun insertShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.insert(shop)
        }
    }

    fun updateShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.update(shop)
        }
    }

    fun deleteShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.delete(shop)
        }
    }
}