package com.example.routes

import com.example.models.ProductDto
import com.example.models.ProductResponseDto
import com.example.service.retrofit.RetroFitRest
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import retrofit2.HttpException

fun Application.productsRoutes(){
    val client : RetroFitRest by inject(qualifier = named("apiProduct"))
    val json = Json { prettyPrint=true }

    routing {
        route("/product"){

            get("/guitar", {
                description = "Conseguir los productos con la categoría GUITAR"
                response {
                    default {
                        description = "Lista con todos los productos con categoría GUITAR"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de los productos"
                        body<List<ProductResponseDto>> { description = "Lista de los productos con categoría GUITAR encontrados" }
                    }
                    HttpStatusCode.Unauthorized to{
                        description = "El token no sea válido"
                    }
                }
            }) {
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAllGuitars(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllGuitarsByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            get("/bass_guitar", {
                description = "Conseguir los productos con la categoría BASS GUITAR"
                response {
                    default {
                        description = "Lista con todos los productos con categoría BASS GUITAR"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de los productos"
                        body<List<ProductResponseDto>> { description = "Lista de los productos con categoría BASS GUITAR encontrados" }
                    }
                    HttpStatusCode.Unauthorized to{
                        description = "El token no sea válido"
                    }
                }
            }){
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAllBassGuitar(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllBassGuitarByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            get("/booster", {
                description = "Conseguir los productos con la categoría BOOSTER"
                response {
                    default {
                        description = "Lista con todos los productos con categoría BOOSTER"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de los productos"
                        body<List<ProductResponseDto>> { description = "Lista de los productos con categoría BOOSTER encontrados" }
                    }
                    HttpStatusCode.Unauthorized to{
                        description = "El token no sea válido"
                    }
                }
            }){
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAllBoosters(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllBoostersByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            get("/accessory", {
                description = "Conseguir los productos con la categoría ACCESSORY"
                response {
                    default {
                        description = "Lista con todos los productos con categoría ACCESSORY"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de los productos"
                        body<List<ProductResponseDto>> { description = "Lista de los productos con categoría ACCESORY encontrados" }
                    }
                    HttpStatusCode.Unauthorized to{
                        description = "El token no sea válido"
                    }
                }
            }){
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAllAccessory(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllAccessoryByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            get ({
                description = "Conseguir todos los productos"
                response {
                    default {
                        description = "Lista con todos los productos"
                    }
                    HttpStatusCode.OK to {
                        description = "Listas de productos"
                        body<List<ProductResponseDto>> { description = "Lista de todos los productos" }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Token inválido"
                    }
                }
            }){
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAllProducts(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllProductsByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            get("/{id}", {
                description = "Encontrar Producto por id"
                request {
                    pathParameter<String>("id") {
                        description = "UUID del producto a buscar"
                        required = true
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Producto con el id indicado"
                        body<ProductResponseDto> { description = "Producto encontrado" }
                    }
                    HttpStatusCode.NotFound to {
                        description = "No se ha encontrado el Producto"
                        body<String> { description = "El id no existe" }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "El token no es válido"
                    }
                }
            }) {
                val token = call.request.headers["Authorization"]
                val uuid = call.parameters["id"].toString()

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getProductById(uuid,token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getProductByIdByUser(uuid)}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }


            authenticate {
                post({
                    description = "Insertar un Producto"
                    request {
                        body<ProductDto> {
                            description = "Datos del producto que queremos crear"
                        }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Producto añadido"
                            body<ProductResponseDto> { description = "Producto que ha sido creado" }
                        }
                        HttpStatusCode.BadRequest to {
                            description = "Validaciones del producto incorrectas"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes los permisos para realizar el insertado"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val product = call.receive<ProductDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.creteProduct(token,product) }.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }


                delete("/{id}", {
                    description = "Eliminar un producto"
                    request {
                        pathParameter<String>("id"){
                            description = "UUID del Producto que queremos eliminar"
                            required = true
                        }
                    }
                    response{
                        HttpStatusCode.NoContent to {
                            description="El producto ha sido eliminado correctamente"
                        }
                        HttpStatusCode.NotFound to {
                            description = "No se ha encontrado el producto con ese id"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes los permisos para realizar la eliminación"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteProduct(uuid,token) }.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }


                put("/{id}", {
                    description = "Actualizar un producto"
                    request {
                        pathParameter<String>("id") {
                            description = "UUID del producto a actualizar"
                            required = true
                        }
                        body<ProductDto> {
                            description = "Datos del producto a actualizar"
                        }
                    }
                    response {
                        HttpStatusCode.OK to{
                            description = "Producto actualizado correctamente"
                            body<ProductResponseDto> {description = "Producto actualizado"}
                        }
                        HttpStatusCode.NotFound to {
                            description= "No se ha encontrado el producto con el id indicado"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="Validación de los datos inválida"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la actualización"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()
                    val product = call.receive<ProductDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.updateProduct(uuid,token,product) }.await()

                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
            }
        }

        route("/storage/product"){
            authenticate {

                post("/{id}", {
                    description = "Insertar una imagen a un producto"
                    request {
                        pathParameter<String>("id") {
                            description = "UUID del producto a actualizar la imagen"
                            required = true
                        }
                        multipartBody {
                            description = "Imagen a poner en el producto"
                        }
                    }
                    response {
                        HttpStatusCode.Created to{
                            description = "Imagen creada correctamente"
                        }
                        HttpStatusCode.NotFound to {
                            description= "No se ha encontrado el producto con el id indicado"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="No se puede almacenar ficheros vacios"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val multipart = call.receiveMultipart().readPart() as PartData.FileItem
                    val uuid = call.parameters["id"].toString()

                    val requestBody = RequestBody.create(MediaType.parse(multipart.contentType.toString()),multipart.streamProvider().readBytes())
                    val multipartBody = MultipartBody.Part.createFormData("file",multipart.originalFileName,requestBody)

                    val myScope = CoroutineScope(Dispatchers.IO)

                    val res = myScope.async { client.saveFileProduct(uuid,token,multipartBody)}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }


                get("/{filename}", {
                    description = "Pedir una imagen a un producto"
                    request {
                        pathParameter<String>("filename") {
                            description = "nombre y extensión de la imagen que buscamos"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to{
                            description = "Imagen encontrada correctamente"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="No se puede determinar el tipo del fichero"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val filename = call.parameters["filename"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    try {
                        val res = myScope.async { client.getFileProduct(filename,token)}.await()
                        call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=$filename")
                        call.respondBytes(res.bytes())
                    }catch (e: HttpException){
                        call.respond(HttpStatusCode.fromValue(e.code()), json.parseToJsonElement(e.response()?.errorBody()?.string()!!))
                    }

                }


                delete("/{filename}", {
                    description = "Eliminar una imagen"
                    request {
                        pathParameter<String>("filename") {
                            description = "nombre y extensión de la imagen que buscamos"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to{
                            description = "Imagen eliminada correctamente"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="Problemas con el storage"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val filename = call.parameters["filename"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteFileProduct(filename,token)}.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
            }
        }
    }
}