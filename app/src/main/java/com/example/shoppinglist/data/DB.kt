package com.example.shoppinglist.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.data.items.ItemDAO
import com.example.shoppinglist.data.shops.ShopDAO

//@Database(entities = [Item::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun itemDAO() : ItemDAO
    abstract fun shopDAO() : ShopDAO

    companion object {
        private var instance: DB? = null

        fun getDatabase(context: Context): DB{
            if(instance!=null){
                return instance as DB
            }
            instance = Room.databaseBuilder(
                context,
                DB::class.java,
                "Shopping List Database"
            ).fallbackToDestructiveMigration().build()
            return instance as DB
        }

    }
}