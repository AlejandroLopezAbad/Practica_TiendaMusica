package com.example.service.retrofitOrder

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.annotation.Single
import retrofit2.Retrofit

class RetroFitClientOrder {

    companion object{
        const val API_ORDER = "http://api-pedidos:8083"
    }

    @Single
    @OptIn(ExperimentalSerializationApi::class)
    fun getInstance(url: String): RetroFitRestPedidos {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder().baseUrl(url)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(RetroFitRestPedidos::class.java)
    }

//    suspend fun login(login: UserLogin): String = withContext(Dispatchers.IO) {
//        val response = getInstance().login(login)
//        val token = response.body()?.token ?: return@withContext "No existe"
//        return@withContext token
//    }
}