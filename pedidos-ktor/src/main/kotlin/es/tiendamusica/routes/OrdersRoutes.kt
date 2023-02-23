package es.tiendamusica.routes

import es.tiendamusica.controllers.OrderController
import es.tiendamusica.dtos.OrderCreateDto
import es.tiendamusica.dtos.OrderPageDto
import es.tiendamusica.dtos.OrderUpdateDto
import es.tiendamusica.exceptions.OrderBadRequest
import es.tiendamusica.exceptions.OrderDuplicated
import es.tiendamusica.exceptions.OrderNotFoundException
import es.tiendamusica.exceptions.OrderUnauthorized
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

private const val ENDPOINT = "/pedidos"
private val logger = KotlinLogging.logger { }
fun Application.ordersRoutes() {

    val ordersService: OrderController by inject()

    routing {
        route(ENDPOINT) {

            //Recupera todos los pedidos guardados.
            get {
                logger.debug { "GET ALL $ENDPOINT" }
                val page = call.request.queryParameters["page"]?.toIntOrNull()
                val perPage = call.request.queryParameters["perPage"]?.toIntOrNull() ?: 10
                if (page != null && page > 0) {
                    val res = ordersService.getAllOrders(page - 1, perPage)
                        .toList()
                        .map { it.toDto() }
                        .let { res -> call.respond(HttpStatusCode.OK, OrderPageDto(page, perPage, res)) }
                }

                ordersService.getAllOrders().toList().let { res -> call.respond(HttpStatusCode.OK, res) }
            }

            //Busca un pedido por su ID
            //En caso de no encontrarlo envía un Not Found
            get("{id}") {
                logger.debug { "GET BY ID : $ENDPOINT/{id}" }
                try {
                    val id = call.parameters["id"]
                    val pedido = ordersService.getById(id!!)
                    pedido?.let {
                        call.respond(HttpStatusCode.OK, pedido.toDto())

                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not found")

                    }
                } catch (e: OrderNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }

            //Busca los pedidos asociados al id de un usuario
            // Si ese usuario no existe envía un Not Found
            get("/user/{user_id}") {
                logger.debug { "GET BY USER ID : $ENDPOINT/{user_id}" }
                try {
                    val id = call.parameters["user_id"]
                    val res =
                        ordersService.getByUserId(id!!).toList().let { res ->
                            println("------------ $res---------")
                            call.respond(HttpStatusCode.OK, res)
                        }
                } catch (e: OrderNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }
            //Cre un pedido nuevo. Si lo consigue crear nos envía un Created
            //Fracasa si el pedido ya existe con ese ID o si no tiene autorización
            post {
                logger.debug { "POST ORDER : $ENDPOINT" }
                try {
                    val dto = call.receive<OrderCreateDto>()
                    val pedido = ordersService.createOrder(dto.toModel())
                    call.respond(HttpStatusCode.Created, pedido.toDto())
                } catch (e: OrderUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: OrderDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                } catch (e: OrderBadRequest) {
                    call.respond(HttpStatusCode.BadRequest, e.message.toString())
                }
            }

            //Modifica un pedido buscado por su ID. Si el pedido no existe devuelve un Not Found
            //Se puede modificar el precio del pedido y el estado.
            patch("{id}") {
                logger.debug { "PATCH ORDER : $ENDPOINT/{id}" }
                try {
                    val dto = call.receive<OrderUpdateDto>()
                    val id = call.parameters["id"]
                    val pedido = ordersService.getById(id!!)
                    pedido?.let {
                        ordersService.patchOrder(pedido, dto)
                        call.respond(HttpStatusCode.OK)
                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not found")
                    }
                } catch (e: OrderUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: OrderDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }

            //Elimina un pedido buscado por su ID
            delete("{id}") {
                logger.debug { "DELETE ORDER : $ENDPOINT/{id}" }
                try {
                    val id = call.parameters["id"]
                    val pedido = ordersService.getById(id!!)
                    pedido?.let {
                        val res = ordersService.deleteOrder(pedido)
                        if (res) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(HttpStatusCode.Unauthorized)
                        }

                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not found")

                    }
                } catch (e: OrderUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                }
            }
        }
    }
}

