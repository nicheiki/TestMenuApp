package com.example.hammerfood.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hammerfood.data.database.FoodItem
import com.example.hammerfood.data.repositories.Repository
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: Repository) : ViewModel() {

    var selectedLocation: MutableLiveData<String> =
        MutableLiveData(repository.getSelectedLocation())
    var selectedCategory: MutableLiveData<String> =
        MutableLiveData(repository.getSelectedCategory())

    var menu: MutableLiveData<MutableList<FoodItem>> = MutableLiveData()

    init {
        viewModelScope.launch { fetchMenu() }
    }

    fun selectLocation(location: String) {
        selectedLocation.value = location
        repository.setSelectedLocation(location)
    }

    /* При отсутствии переданных параметров метод выбирает текущую категорию.*/
    fun selectCategory(category: String? = selectedCategory.value) {
        selectedCategory.value = category
        category?.let {
            repository.setSelectedCategory(it)
            viewModelScope.launch { menu.postValue(repository.getMenu(it)) }
        }
    }

    suspend fun fetchMenu() {
        try {
            val list = repository.fetchMenu()
            repository.clearMenu()
            repository.setMenu(list)
        } catch (e: Exception) {
        }
    }
}


class MenuViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MenuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
