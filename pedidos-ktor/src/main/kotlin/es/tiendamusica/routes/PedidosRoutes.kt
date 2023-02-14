package es.tiendamusica.routes

import es.tiendamusica.dtos.PedidoCreateDto
import es.tiendamusica.exceptions.PedidoDuplicated
import es.tiendamusica.exceptions.PedidoNotFoundException
import es.tiendamusica.exceptions.PedidoUnauthorized
import es.tiendamusica.mappers.toDto
import es.tiendamusica.mappers.toUUID
import es.tiendamusica.models.toModel
import es.tiendamusica.repository.pedidos.PedidosRepository
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
            //-------- POST -------------
            post {
                logger.debug { "POST PEDIDO : $ENDPOINT" }
                try {
                    val dto = call.receive<PedidoCreateDto>()
                    val pedido = pedidosService.save(dto.toModel())
                    call.respond(HttpStatusCode.Created, pedido.toDto())
                } catch (e: PedidoUnauthorized) {
                    call.respond(HttpStatusCode.Unauthorized, e.message.toString())
                } catch (e: PedidoDuplicated) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }
        }
    }
}