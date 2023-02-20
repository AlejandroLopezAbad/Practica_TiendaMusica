package es.tiendamusica

import es.tiendamusica.models.User
import es.tiendamusica.plugins.configureRouting
import es.tiendamusica.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule


class KoinApp : KoinComponent {
    fun run() {
        val user = User("Jeremy")


        /*val productos1 = mutableListOf<LineaVenta>()
        repeat(3) {
            productos1.add(
                LineaVenta(UUID.randomUUID().toString(), (1..100).random().toDouble(), (1..15).random())
            )
        }
        val productos2 = mutableListOf<LineaVenta>()
        repeat(3) {
            productos2.add(
                LineaVenta(UUID.randomUUID().toString(), (1..100).random().toDouble(), (1..15).random())
            )
        }
        val order = Pedido(
            price = productos1.sumOf { it.precio },
            status = Pedido.Status.PROCESS,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            userId = user.name,
            productos = productos1
        )
        val order2 = Pedido(
            price = productos2.sumOf { it.precio },
            status = Pedido.Status.PROCESS,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            userId = user.name,
            productos = productos2
        )*/
        //  val controller = PedidosController(PedidosRepository())
        // controller.createOrder(order)
        //controller.createOrder(order2)

        embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)

    }
}

suspend fun main() {

    startKoin {
        defaultModule()
    }

    KoinApp().run()
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
