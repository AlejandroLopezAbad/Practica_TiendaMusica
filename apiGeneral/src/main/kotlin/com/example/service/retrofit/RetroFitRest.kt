package com.example.service.retrofit
import com.example.models.*
import retrofit2.Response
import retrofit2.http.*

interface RetroFitRest {
    /**
     * Servicios
     */
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

    /**
     * Productos
     */
    @GET("api/product/guitar")
    suspend fun getAllGuitars(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("api/product/guitar")
    suspend fun getAllGuitarsByUser(): Response<List<ProductResponseDto>>

    @GET("api/product/bass_guitar")
    suspend fun getAllBassGuitar(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("api/product/bass_guitar")
    suspend fun getAllBassGuitarByUser(): Response<List<ProductResponseDto>>

    @GET("api/product/booster")
    suspend fun getAllBoosters(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("api/product/booster")
    suspend fun getAllBoostersByUser(): Response<List<ProductResponseDto>>

    @GET("api/product/accessory")
    suspend fun getAllAccessory(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("api/product/accessory")
    suspend fun getAllAccessoryByUser(): Response<List<ProductResponseDto>>

    @GET("api/product")
    suspend fun getAllProducts(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("api/product")
    suspend fun getAllProductsByUser(): Response<List<ProductResponseDto>>

    @GET("api/product/{id}")
    suspend fun getProductById(@Path("id") id:String, @Header("Authorization") token: String):Response<ProductResponseDto>

    @GET("api/product/{id}")
    suspend fun getProductByIdByUser(@Path("id") id:String):Response<ProductResponseDto>

    @POST("/api/product")
    suspend fun creteProduct(@Header("Authorization") token: String,@Body service : ProductDto): Response<ProductResponseDto>

    @DELETE("/api/product/{id}")
    suspend fun deleteProduct(@Path("id") id: String, @Header("Authorization") token: String): Response<Void>

    @PUT("/api/product/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Header("Authorization") token: String, @Body service: ProductDto): Response<ProductResponseDto>



}