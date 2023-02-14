package es.tiendamusica

import es.tiendamusica.controllers.PedidosController
import es.tiendamusica.models.Pedido
import es.tiendamusica.models.User
import es.tiendamusica.plugins.configureRouting
import es.tiendamusica.plugins.configureSerialization
import es.tiendamusica.repository.pedidos.PedidosRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.time.LocalDate

suspend fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

    val user = User("Jeremy")


    val order = Pedido(
        price = 40.0,
        status = Pedido.Status.PROCESS,
        createdAt = LocalDate.now(),
        deliveredAt = LocalDate.now(),
        userId = user.name
    )

    val controller = PedidosController(PedidosRepository())

    controller.createOrder(order)


}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
