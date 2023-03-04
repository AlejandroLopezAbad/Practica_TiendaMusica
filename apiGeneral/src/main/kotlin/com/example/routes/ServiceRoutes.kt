package com.example.routes

import com.example.models.ServiceCreateDto
import com.example.models.ServiceUpdateDto
import com.example.service.retrofit.RetroFitRest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Application.serviciosRoutes(){
    val client : RetroFitRest by inject()
    val json = Json { prettyPrint=true }

    routing {
        route("/service"){
            get {
                val token = call.request.headers["Authorization"]

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getAll(token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getAllByUser()}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }
            get("/{id}") {
                val token = call.request.headers["Authorization"]
                val uuid = call.parameters["id"].toString()

                val myScope = CoroutineScope(Dispatchers.IO)
                if (token != null){
                    val res = myScope.async { client.getById(uuid,token.toString())}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }else{
                    val res = myScope.async { client.getByIdByUser(uuid)}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else{
                        call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                    }
                }
            }
            authenticate {
                post {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val service = call.receive<ServiceCreateDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.creteService(token,service) }.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
                delete("/{id}") {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteService(uuid,token) }.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }

                put("/{id}") {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()
                    val service = call.receive<ServiceUpdateDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.updateService(uuid,token,service) }.await()

                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.OK,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
            }
        }
    }
}