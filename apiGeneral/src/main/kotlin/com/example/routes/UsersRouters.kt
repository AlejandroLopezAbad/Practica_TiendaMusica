package com.example.routes

import com.example.service.retrofit.RetroFitRest
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.UsersRoutes(){
    val client : RetroFitRest by inject(qualifier = named("MicroServicioUsuarios"))
    val json = Json { prettyPrint=true }

    routing {

        route("/users"){
            post("/login") {


            }



        }
    }
}