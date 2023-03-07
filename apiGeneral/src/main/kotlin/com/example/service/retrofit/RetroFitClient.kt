package com.example.service.retrofit

import com.example.service.retrofitOrder.RetroFitRestPedidos
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.annotation.Single
import retrofit2.Retrofit

class RetroFitClient {

    companion object{
        const val API_PRODUCT = "http://api-productos:8082"
        const val API_USERS = "http://api-usuarios:8081"
    }

    @Single
    @OptIn(ExperimentalSerializationApi::class)
    fun getInstance(url: String): RetroFitRest {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder().baseUrl(url)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(RetroFitRest::class.java)
    }

}