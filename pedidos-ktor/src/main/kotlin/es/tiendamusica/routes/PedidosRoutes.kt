package es.tiendamusica.routes

import es.tiendamusica.exceptions.PedidoNotFoundException
import es.tiendamusica.mappers.toDto
import es.tiendamusica.mappers.toUUID
import es.tiendamusica.repository.pedidos.PedidosRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.koin.ktor.ext.inject

private const val ENDPOINT = "/pedidos"
private val logger = KotlinLogging.logger { }
fun Application.pedidosRoutes() {
    val pedidosService: PedidosRepository by inject()

    routing {
        route(ENDPOINT) {

            //--------- GETS -------------
            get {
                logger.debug { "GET ALL $ENDPOINT" }
                val res = pedidosService.findAll()
                    .toList().map { it.toDto() }
                    .let { res -> call.respond(HttpStatusCode.OK, res) }
            }

            //GET BY ID
            get("{id}") {
                logger.debug { "GET BY ID : $ENDPOINT/{id}" }
                try {
                    val id = call.parameters["id"]?.toUUID()
                    val pedido = pedidosService.findById(id!!)
                    call.respond(HttpStatusCode.OK, pedido!!.toDto())
                } catch (e: PedidoNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }

            //GET BY USER
            get("{user_id}") {
                logger.debug { "GET BY USER ID : $ENDPOINT/{user_id}" }
                try {
                    val id = call.parameters["id"]?.toUUID()
                    //TODO(cambiar el id de pedido por id de usuario)
                    val pedido = pedidosService.findById(id!!)
                    call.respond(HttpStatusCode.OK, pedido!!.toDto())
                } catch (e: PedidoNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }
            //---------------------------
        }
    }
}