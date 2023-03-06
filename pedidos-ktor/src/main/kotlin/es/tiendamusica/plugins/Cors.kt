package es.tiendamusica.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCors() {
    install(CORS) {
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
}