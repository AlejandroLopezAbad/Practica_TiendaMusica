package com.example.routes

import com.example.models.OrderCreateDto
import com.example.models.OrderUpdateDto
import com.example.models.ProductDto
import com.example.models.ProductResponseDto
import com.example.service.retrofit.RetroFitRest
import com.example.service.retrofitOrder.RetroFitRestPedidos
import es.tiendamusica.exceptions.OrderBadRequest
import es.tiendamusica.exceptions.OrderNotFoundException
import es.tiendamusica.exceptions.OrderUnauthorized
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
import org.koin.core.qualifier.qualifier
import org.koin.ktor.ext.inject

fun Application.orderRoutes() {
    val client: RetroFitRestPedidos by inject(qualifier = named("apiOrder"))
    val clientProducts: RetroFitRest by inject(qualifier = named("apiProduct"))
    val json = Json { ignoreUnknownKeys = true }

    routing {
        route("/pedidos") {
            //TODO(BORRA ESTO DESTPUÉS DE EXPLICARLO)
            /*get("/hello") {
                val res = client.tryApiConnection()
                call.respond(HttpStatusCode.OK, res.message())
            }*/
            get {
                print("ENTRA")
                val token = call.request.headers["Authorization"]
                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null) {
                    val res = myScope.async { client.getAllOrders(token) }.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null) {
                        call.respond(HttpStatusCode.OK, body)
                    } else call.respond(
                        HttpStatusCode.fromValue(res.code()),
                        json.parseToJsonElement(res.errorBody()?.string()!!)
                    )
                }
            }
            get("/{id}") {
                val token = call.request.headers["Authorization"]
                val uuid = call.parameters["id"].toString()

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null) {
                    val res = myScope.async { client.getOrderById(uuid, token.toString()) }.await()
                    val body = res.body()
                    try {
                        if (res.isSuccessful && body != null) {
                            call.respond(HttpStatusCode.OK, body)
                        }
                    } catch (e: OrderNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, e.message.toString())
                    }
                }
            }

            get("/user/{user_id}") {
                val token = call.request.headers["Authorization"]
                val id = call.parameters["user_id"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null) {
                    try {
                        val res = myScope.async { client.getAllOrdersByUser(id!!, token) }.await()
                        if (res.isSuccessful && res.body()!!.size > 0) {
                            call.respond(HttpStatusCode.OK, res.body()!!)
                        }
                    } catch (e: OrderNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, e.message.toString())
                    }
                }
            }

            authenticate {
                post {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val service = call.receive<OrderCreateDto>()
                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.creteOrder(service, token!!) }.await()
                    val body = res.body()
                    try {
                        service.products.forEach {
                            val res = myScope.async {
                                clientProducts.getProductById(it.idItem, token)
                            }.await()
                            println(res.code())
                            if (!res.isSuccessful || body == null)
                                println(it)
                            call.respond(HttpStatusCode.NotFound, "Productos no encontrados")
                            val product = res.body()
                            if (product?.stock!! < it.quantity)
                                call.respond(HttpStatusCode.BadRequest)
                        }
                        if (res.isSuccessful && body != null) {
                            service.products.forEach {
                                val res = myScope.async {
                                    clientProducts.getProductById(it.idItem, token)
                                }.await()
                                clientProducts.updateProduct(
                                    it.idItem,
                                    token,
                                    ProductDto(
                                        res.body()?.name!!,
                                        res.body()?.price!!,
                                        res.body()?.available!!,
                                        res.body()?.description!!,
                                        res.body()?.url!!,
                                        res.body()?.category!!,
                                        res.body()?.stock!! - it.quantity,
                                        res.body()?.brand!!, res.body()?.model!!
                                    )
                                )
                            }
                            call.respond(HttpStatusCode.Created, body)
                        }
                    } catch (e: OrderBadRequest) {
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }
            }

            delete("/{id}") {
                val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                val id = call.parameters["id"].toString()

                val myScope = CoroutineScope(Dispatchers.IO)
                val res = myScope.async { client.deleteProduct(id, token) }.await()
                try {
                    if (res.isSuccessful) {
                        call.respond(HttpStatusCode.NoContent)
                    }
                } catch (e: OrderUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                }
            }

            patch("/{id}") {
                val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                val id = call.parameters["id"].toString()
                val updated = call.receive<OrderUpdateDto>()
                val myScope = CoroutineScope(Dispatchers.IO)
                val res = myScope.async { client.updateProduct(id, token, updated) }.await()
                if (res.isSuccessful) {
                    call.respond(HttpStatusCode.OK)
                } else call.respond(
                    HttpStatusCode.fromValue(res.code()),
                    json.parseToJsonElement(res.errorBody()?.string()!!)
                )
            }
        }
    }
}
