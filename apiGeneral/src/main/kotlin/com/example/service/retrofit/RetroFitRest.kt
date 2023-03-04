package com.example.service.retrofit
import com.example.models.Service
import com.example.models.ServiceCreateDto
import com.example.models.ServiceDto
import com.example.models.ServiceUpdateDto
import retrofit2.Response
import retrofit2.http.*

interface RetroFitRest {
    @GET("api/service")
    suspend fun getAll(@Header("Authorization") token: String): Response<List<Service>>

    @GET("api/service")
    suspend fun getAllByUser(): Response<List<ServiceDto>>

    @GET("api/service/{id}")
    suspend fun getById(@Path("id") id:String, @Header("Authorization") token: String):Response<Service>

    @GET("api/service/{id}")
    suspend fun getByIdByUser(@Path("id") id:String):Response<ServiceDto>

    @POST("/api/service")
    suspend fun creteService(@Header("Authorization") token: String,@Body service : ServiceCreateDto): Response<Service>

    @DELETE("/api/service/{id}")
    suspend fun deleteService(@Path("id") id: String, @Header("Authorization") token: String): Response<Void>

    @PUT("/api/service/{id}")
    suspend fun updateService(@Path("id") id: String, @Header("Authorization") token: String, @Body service: ServiceUpdateDto): Response<Service>

}