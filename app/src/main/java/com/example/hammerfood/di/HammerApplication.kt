package com.example.hammerfood.di

import android.app.Application
import com.example.hammerfood.data.database.MenuDatabase
import com.example.hammerfood.data.repositories.Repository

class HammerApplication : Application() {

    val repository by lazy {
        return@lazy Repository(
            MenuDatabase.getInstance(this).menuDao,
            getSharedPreferences("preferences", MODE_PRIVATE)
        )
    }
}