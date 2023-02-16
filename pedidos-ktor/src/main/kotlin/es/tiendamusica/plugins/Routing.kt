package es.tiendamusica.plugins

import es.tiendamusica.routes.pedidosRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("ProbandoApi!")
        }

    }
    pedidosRoutes()
}
