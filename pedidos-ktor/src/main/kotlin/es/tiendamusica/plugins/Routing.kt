package es.tiendamusica.plugins

import es.tiendamusica.routes.ordersRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("ProbandoApi!")
        }

    }
    ordersRoutes()
}
