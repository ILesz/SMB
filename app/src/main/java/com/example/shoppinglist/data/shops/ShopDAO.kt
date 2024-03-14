package com.example.shoppinglist.data.shops

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.items.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDAO {
        @Query("SELECT * FROM shop")
        fun getShops(): Flow<List<Shop>>

        @Insert
        fun insetShop(shop: Shop)

        @Update
        fun updateShop(shop: Shop)

        @Delete
        fun deleteShop(shop: Shop)

}