package com.example.hammerfood.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://testmenuapi.herokuapp.com/"

object Client {
    val apiClient: MenuService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        return@lazy retrofit.create(MenuService::class.java)
    }

}