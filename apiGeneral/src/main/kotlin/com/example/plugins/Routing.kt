package com.example.plugins

import com.example.routes.orderRoutes
import com.example.routes.productsRoutes
import com.example.routes.serviciosRoutes
import com.example.routes.usuariosRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    serviciosRoutes()
    productsRoutes()
    orderRoutes()
    usuariosRoutes()
}
