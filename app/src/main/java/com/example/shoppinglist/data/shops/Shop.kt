package com.example.shoppinglist.data.shops

data class Shop (
    var id: String = "new",
    var name: String,
    var description: String,
    var radius: Int,
    var longitude: Double,
    var latitude: Double
)