package com.example.routes

import com.example.models.ProductDto
import com.example.service.retrofit.RetroFitRest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.productsRoutes(){
    val client : RetroFitRest by inject(qualifier = named("apiProduct"))
    val json = Json { prettyPrint=true }

    routing {
        route("/product"){
            get("/guitar"){
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
            get("/bass_guitar"){
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
            get("/booster"){
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
            get("/accessory"){
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
            get {
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
            get("/{id}") {
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
                post {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val product = call.receive<ProductDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.creteProduct(token,product) }.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
                delete("/{id}") {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteProduct(uuid,token) }.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }

                put("/{id}") {
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
    }
}