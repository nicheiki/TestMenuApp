package com.example.hammerfood.data.network

import retrofit2.Response
import retrofit2.http.GET

interface MenuService {

    @GET("menu")
    suspend fun getMenu(): Response<List<Model>>

}