package es.tiendamusica.routes

import es.tiendamusica.controllers.PedidosController
import es.tiendamusica.dtos.PedidoCreateDto
import es.tiendamusica.dtos.UpdatePedidoDto
import es.tiendamusica.exceptions.PedidoDuplicated
import es.tiendamusica.exceptions.PedidoNotFoundException
import es.tiendamusica.exceptions.PedidoUnauthorized
import es.tiendamusica.mappers.toDto
import es.tiendamusica.models.toModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.koin.ktor.ext.inject
import org.litote.kmongo.toId

private const val ENDPOINT = "/pedidos"
private val logger = KotlinLogging.logger { }
fun Application.pedidosRoutes() {

    val pedidosService: PedidosController by inject()

    routing {
        route(ENDPOINT) {

            //--------- GETS -------------
            get {
                logger.debug { "GET ALL $ENDPOINT" }
                pedidosService.getAllOrders().toList().let { res -> call.respond(HttpStatusCode.OK, res) }
            }

            //GET BY ID
            get("{id}") {
                logger.debug { "GET BY ID : $ENDPOINT/{id}" }
                try {
                    val id = call.parameters["id"]
                    val pedido = pedidosService.getById(id!!)
                    pedido?.let {
                        call.respond(HttpStatusCode.OK, pedido.toDto())

                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not found")

                    }
                } catch (e: PedidoNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }

            //GET BY USER
            get("/user/{user_id}") {
                logger.debug { "GET BY USER ID : $ENDPOINT/{user_id}" }
                try {
                    val id = call.parameters["user_id"]
                    val res =
                        pedidosService.getByUserId(id!!).toList().let { res ->
                            println("------------ $res---------")
                            call.respond(HttpStatusCode.OK, res)
                        }
                } catch (e: PedidoNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }
            //---------------------------
            //-------- POST -------------
            post {
                logger.debug { "POST PEDIDO : $ENDPOINT" }
                try {
                    val dto = call.receive<PedidoCreateDto>()
                    val pedido = pedidosService.createOrder(dto.toModel())
                    call.respond(HttpStatusCode.Created, pedido.toDto())
                } catch (e: PedidoUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: PedidoDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }
            patch("{id}") {
                logger.debug { "PATCH EPDIDO : $ENDPOINT/{id}" }
                try {
                    val dto = call.receive<UpdatePedidoDto>()
                    val id = call.parameters["id"]
                    val pedido = pedidosService.getById(id!!)
                    pedido?.let {
                        println("PEDIDO ENCONTRADO----------------------------")
                        pedidosService.patchPedido(pedido, dto)
                        call.respond(HttpStatusCode.OK)
                    } ?: run {
                        println("PEDIDO NO ENCONTRADO----------------------------")

                        call.respond(HttpStatusCode.NotFound, "Not found")
                    }
                } catch (e: PedidoUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: PedidoDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }
            delete("{id}") {
                logger.debug { "DELETE PEDIDO : $ENDPOINT/{id}" }
                try {
                    val id = call.parameters["id"]
                    val pedido = pedidosService.getById(id!!)
                    pedido?.let {
                        val res = pedidosService.deleteOrder(pedido)
                        if (res) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.Unauthorized)
                        }

                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not found")

                    }
                } catch (e: PedidoUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                }
            }
        }
    }
}