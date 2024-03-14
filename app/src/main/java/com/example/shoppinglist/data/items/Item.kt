package com.example.shoppinglist.data.items

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
data class Item(
    //@PrimaryKey(autoGenerate = true)
    //var id: Long = 0,
    var id: String = "new",
    var name: String?,
    var number: Int = 1,
    var price: Float,
    var bought: Boolean
)