package com.example.service.retrofitOrder
import com.example.models.*
import retrofit2.Response
import retrofit2.http.*

interface RetroFitRestPedidos {
    /**
     * Productos
     */
    @GET("/")
    suspend fun tryApiConnection() : Response<String>
    @GET("/pedidos")
    suspend fun getAllOrders(@Header("Authorization") token: String): Response<List<Order>>

    @GET("/pedidos/user/{user_id}")
    suspend fun getAllOrdersByUser(@Path("user_id") id:String, @Header("Authorization") token: String ): Response<List<Order>>

    @GET("/pedidos/{id}")
    suspend fun getOrderById(@Path("id") id:String, @Header("Authorization") token: String):Response<OrderDto>

    @POST("/pedidos")
    suspend fun creteOrder(@Body service : OrderCreateDto, @Header("Authorization") token: String): Response<OrderDto>

    @DELETE("/pedidos/{id}")
    suspend fun deleteProduct(@Path("id") id: String, @Header("Authorization") token: String): Response<Void>

    @PATCH("/pedidos/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Header("Authorization") token: String, @Body order: OrderUpdateDto): Response<Void>


}