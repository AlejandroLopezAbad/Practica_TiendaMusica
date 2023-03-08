package com.example.routes

import com.example.models.*
import com.example.service.retrofit.RetroFitRest
import com.example.service.retrofitOrder.RetroFitRestPedidos
import es.tiendamusica.exceptions.OrderBadRequest
import es.tiendamusica.exceptions.OrderNotFoundException
import es.tiendamusica.exceptions.OrderUnauthorized
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.patch
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
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

            get({
                description = "Muestra todos los pedidos"
                response {
                    default {
                        description = "Muestra una lista con todos los pedidos que hay en el sistema"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de pedidos"
                        body<List<OrderDto>> { description = "Lista de todos los pedidos" }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Token inválido"
                    }
                }
            }) {
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
            get("/{id}", {
                description = "Busca un pedido por su ID"
                request {
                    pathParameter<String>("id") {
                        description = "Id del pedido "
                        required = true
                    }
                }
                response {
                    default {
                        description = "Muestra un pedido buscado por el id."
                    }
                    HttpStatusCode.OK to {
                        description = "El pedido con el id indicado"
                        body<OrderDto> { description = "Pedido encontrado" }
                    }
                    HttpStatusCode.NotFound to {
                        description = "Pedido no encotnrado"
                    }
                }
            }) {
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

            get("/user/{user_id}", {
                description = "Busca los pedidos de un usuario"
                request {
                    pathParameter<String>("id") {
                        description = "Id del usuario "
                        required = true
                    }
                }
                response {
                    default {
                        description = "Muestra los pedidos que pertenecen al usuario con el id dado ."
                    }
                    HttpStatusCode.OK to {
                        description = "Los pedidos de un usuario"
                        body<List<OrderDto>> { description = "Pedidos encontrados" }
                    }
                    HttpStatusCode.NotFound to {
                        description = "Pedidos no encotnrado"
                    }
                }
            }) {
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
                post({
                    description = "Crea un pedido"
                    request {
                        body<OrderCreateDto> {
                            description = "Datos del pedido a crear"
                        }
                    }
                    response {
                        default {
                            description = "Crea un pedido con los datos dados"
                        }
                        HttpStatusCode.Created to {
                            description = "Se ha creado el pedido"
                        }
                        HttpStatusCode.BadRequest to {
                            description = "Pedidos no creado"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()

                    val service = call.receive<OrderCreateDto>()
                    val myScope = CoroutineScope(Dispatchers.IO)



                    val res = myScope.async { client.creteOrder(service, token) }.await()
                    val body = res.body()
                    try {
                        if (res.isSuccessful && body != null) {
                            client.creteOrder(service, token)
                            service.products.forEach {
                                val prod = async {  clientProducts.getProductById(it.idItem, token)}.await()
                                if(prod.isSuccessful){
                                    clientProducts.updateProduct(it.idItem, token, ProductDto(
                                        prod.body()?.name!!,
                                        prod.body()?.price!!,
                                        prod.body()?.available!!,
                                        prod.body()?.description!!,
                                        prod.body()?.url!!,
                                        prod.body()?.category!!,
                                        prod.body()?.stock!! - it.quantity,
                                        prod.body()?.brand!!,
                                        prod.body()?.model!!
                                    ))
                                }
                            }
                            call.respond(HttpStatusCode.Created, body)
                        }
                    } catch (e: OrderBadRequest) {
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }
            }

            delete("/{id}", {
                description = "Elimina un pedido"
                request {
                    pathParameter<String>("id") {
                        description = "Id del pedido "
                        required = true
                    }
                }
                response {
                    default {
                        description = "El pedido se elimna"
                    }
                    HttpStatusCode.NoContent to {
                        description = "Se ha eliminado el pedido"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Token no valido"
                    }
                }
            }) {
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

            patch("/{id}", {
                description = "Actualiza un pedido"
                request {
                    pathParameter<String>("id") {
                        description = "Id del pedido "
                        required = true
                    }
                    body<OrderUpdateDto> {
                        description = "Datos del pedido a actualizar"
                    }
                }
                response {
                    HttpStatusCode.OK to{
                        description = "Pedido actualizado correctamente"
                        body<OrderDto> {description = "Pedido actualizado"}
                    }
                    HttpStatusCode.Unauthorized to {
                        description= "Token invalido"
                    }
                    HttpStatusCode.BadRequest to{
                        description ="Peiddo no actualizado"
                    }
                }
            }) {
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
