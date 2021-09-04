package com.example.hammerfood.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(foodItems: List<FoodItem>)

    @Query("SELECT * FROM menu WHERE category = :category ORDER BY id ASC")
    suspend fun getMenu(category: String): MutableList<FoodItem>

    @Query("DELETE FROM menu")
    suspend fun clear()

}
