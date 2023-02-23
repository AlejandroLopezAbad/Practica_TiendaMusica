package es.tiendamusica

import es.tiendamusica.controllers.OrderController
import es.tiendamusica.dtos.SellLine
import es.tiendamusica.models.Order
import es.tiendamusica.models.User
import es.tiendamusica.plugins.*
import es.tiendamusica.repository.pedidos.OrderRepository
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.core.component.KoinComponent
import java.time.LocalDate
import java.util.*


class KoinApp : KoinComponent {
    suspend fun run() {
        val user = User(UUID.randomUUID().toString())


        val productos1 = mutableListOf<SellLine>()
        repeat(3) {
            productos1.add(
                SellLine(UUID.randomUUID().toString(), (1..100).random().toDouble(), (1..15).random())
            )
        }
        val productos2 = mutableListOf<SellLine>()
        repeat(3) {
            productos2.add(
                SellLine(UUID.randomUUID().toString(), (1..100).random().toDouble(), (1..15).random())
            )
        }
        val order = Order(
            price = productos1.sumOf { it.price * it.quantity },
            status = Order.Status.PROCESSING,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            userId = user.name,
            productos = productos1
        )
        val order2 = Order(
            price = productos2.sumOf { it.price * it.quantity },
            status = Order.Status.PROCESSING,
            createdAt = LocalDate.now(),
            deliveredAt = LocalDate.now(),
            userId = user.name,
            productos = productos2
        )
          val controller = OrderController(OrderRepository())
         controller.createOrder(order)
        controller.createOrder(order2)


    }

}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRouting()
    configureCors()
    configuteValidations()
}
