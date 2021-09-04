package com.example.hammerfood.data.network

import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("id") val id: Int,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("price") val price: String,
    @SerializedName("url") val url: String
)