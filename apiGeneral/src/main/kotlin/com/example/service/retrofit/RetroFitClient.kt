package com.example.service.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton
import retrofit2.Retrofit

class RetroFitClient {

    companion object{
        const val API_PRODUCT = "http://localhost:8082"
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

//    suspend fun login(login: UserLogin): String = withContext(Dispatchers.IO) {
//        val response = getInstance().login(login)
//        val token = response.body()?.token ?: return@withContext "No existe"
//        return@withContext token
//    }
}