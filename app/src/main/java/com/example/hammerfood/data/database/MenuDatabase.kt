package com.example.hammerfood.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {

    abstract val menuDao: MenuDao

    companion object {

        @Volatile
        private var INSTANCE: MenuDatabase? = null

        fun getInstance(context: Context): MenuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MenuDatabase::class.java,
                    "menu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}

