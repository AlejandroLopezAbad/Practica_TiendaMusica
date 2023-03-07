package com.example.service.retrofit
import com.example.models.*
import io.ktor.http.*
import io.ktor.http.content.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface RetroFitRest {
    /**
     * Usuarios
     */

    @POST("/api/users/register")
    suspend fun registerUser(@Body user : UserCreateDto) : Response<UserTokenDto>
    @POST("/api/users/login")
    suspend fun loginUser(@Body user : UserLoginDto) : Response<UserTokenDto>

    @GET("/api/users/list")
    suspend fun getAllUsers(@Header("Authorization") token: String) : Response<List<UserDto>>
    @GET("/api/users/me")
    suspend fun getUserMe(@Header("Authorization") token : String) : Response<UserDto>
    @Multipart
    @PATCH("/api/users/me")
    suspend fun updateAvatarUsuario(@Header("Authorization") token: String, @Part file: MultipartBody.Part): Response<UserDto>




    /**
     * Servicios
     */
    @GET("/api/service")
    suspend fun getAll(@Header("Authorization") token: String): Response<List<Service>>

    @GET("/api/service")
    suspend fun getAllByUser(): Response<List<ServiceDto>>

    @GET("/api/service/{id}")
    suspend fun getById(@Path("id") id:String, @Header("Authorization") token: String):Response<Service>

    @GET("/api/service/{id}")
    suspend fun getByIdByUser(@Path("id") id:String):Response<ServiceDto>

    @POST("/api/service")
    suspend fun creteService(@Header("Authorization") token: String,@Body service : ServiceCreateDto): Response<Service>

    @DELETE("/api/service/{id}")
    suspend fun deleteService(@Path("id") id: String, @Header("Authorization") token: String): Response<Void>

    @PUT("/api/service/{id}")
    suspend fun updateService(@Path("id") id: String, @Header("Authorization") token: String, @Body service: ServiceUpdateDto): Response<Service>

    @Multipart
    @POST("/api/storage/service/{id}")
    suspend fun saveFileService(@Path("id") id: String, @Header("Authorization") token: String, @Part file: MultipartBody.Part): Response<Map<String, String>>

    @GET("/api/storage/service/{filename}")
    suspend fun getFileService(@Path("filename") filename: String, @Header("Authorization") token: String): ResponseBody

    @DELETE("/api/storage/service/{filename}")
    suspend fun deleteFileService(@Path("filename") filename: String, @Header("Authorization") token: String): Response<Void>

    /**
     * Productos
     */
    @GET("/api/product/guitar")
    suspend fun getAllGuitars(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("/api/product/guitar")
    suspend fun getAllGuitarsByUser(): Response<List<ProductUserResponseDto>>

    @GET("/api/product/bass_guitar")
    suspend fun getAllBassGuitar(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("/api/product/bass_guitar")
    suspend fun getAllBassGuitarByUser(): Response<List<ProductUserResponseDto>>

    @GET("/api/product/booster")
    suspend fun getAllBoosters(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("/api/product/booster")
    suspend fun getAllBoostersByUser(): Response<List<ProductUserResponseDto>>

    @GET("/api/product/accessory")
    suspend fun getAllAccessory(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("/api/product/accessory")
    suspend fun getAllAccessoryByUser(): Response<List<ProductUserResponseDto>>

    @GET("/api/product")
    suspend fun getAllProducts(@Header("Authorization") token: String): Response<List<ProductResponseDto>>

    @GET("/api/product")
    suspend fun getAllProductsByUser(): Response<List<ProductUserResponseDto>>

    @GET("/api/product/{id}")
    suspend fun getProductById(@Path("id") id:String, @Header("Authorization") token: String):Response<ProductResponseDto>

    @GET("/api/product/{id}")
    suspend fun getProductByIdByUser(@Path("id") id:String):Response<ProductUserResponseDto>

    @POST("/api/product")
    suspend fun creteProduct(@Header("Authorization") token: String,@Body service : ProductDto): Response<ProductResponseDto>

    @DELETE("/api/product/{id}")
    suspend fun deleteProduct(@Path("id") id: String, @Header("Authorization") token: String): Response<Void>

    @PUT("/api/product/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Header("Authorization") token: String, @Body service: ProductDto): Response<ProductResponseDto>



    @Multipart
    @POST("/api/storage/product/{id}")
    suspend fun saveFileProduct(@Path("id") id: String, @Header("Authorization") token: String, @Part file: MultipartBody.Part): Response<Map<String, String>>

    @GET("/api/storage/product/{filename}")
    suspend fun getFileProduct(@Path("filename") filename: String, @Header("Authorization") token: String): ResponseBody

    @DELETE("/api/storage/product/{filename}")
    suspend fun deleteFileProduct(@Path("filename") filename: String, @Header("Authorization") token: String): Response<Void>


    /*
    Usuarios
     */
}