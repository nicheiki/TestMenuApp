package com.example.hammerfood.data.repositories

import android.content.SharedPreferences
import com.example.hammerfood.data.database.FoodItem
import com.example.hammerfood.data.database.MenuDao
import com.example.hammerfood.data.network.Client
import com.example.hammerfood.data.network.Model

class Repository(private val menuDao: MenuDao, private val sharedPreferences: SharedPreferences) {

    fun getSelectedLocation(): String? =
        sharedPreferences.getString("selectedLocation", "")

    fun setSelectedLocation(location: String) =
        sharedPreferences.edit().putString("selectedLocation", location).apply()

    fun getSelectedCategory(): String? =
        sharedPreferences.getString("selectedCategory", "")

    fun setSelectedCategory(category: String) =
        sharedPreferences.edit().putString("selectedCategory", category).apply()

    suspend fun getMenu(category: String) = menuDao.getMenu(category)
    suspend fun setMenu(list: List<FoodItem>) = menuDao.insertList(list)
    suspend fun clearMenu() = menuDao.clear()

    suspend fun fetchMenu() = Client.apiClient.getMenu().body()!!.mapToFoodItems()

    private fun List<Model>.mapToFoodItems(): List<FoodItem> {
        return this.map {
            FoodItem(it.id, it.category, it.title, it.text, it.price, it.url)
        }
    }

}