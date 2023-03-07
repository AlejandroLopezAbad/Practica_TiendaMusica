package com.example.routes

import com.example.models.UserCreateDto
import com.example.models.UserLoginDto
import com.example.service.retrofit.RetroFitRest
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun Application.usuariosRoutes() {
    val client: RetroFitRest by inject(qualifier = named("apiUsuarios"))
    val json = Json { prettyPrint = true }
    routing {
        route("/users") {
            post("/register") {
                val dto = call.receive<UserCreateDto>()
                val res = async(Dispatchers.IO) {
                    client.registerUser(dto)
                }.await()
                val body = res.body()
                if (res.isSuccessful && body != null) {
                    call.respond(HttpStatusCode.Created, body)
                } else
                    call.respond(
                        HttpStatusCode.fromValue(res.code()),
                        json.parseToJsonElement(res.errorBody()?.string()!!)
                    )
            }

            post("/login") {
                val dto = call.receive<UserLoginDto>()
                val res = async(Dispatchers.IO) {
                    client.loginUser(dto)
                }.await()
                val body = res.body()
                if (res.isSuccessful && body != null) {
                    call.respond(HttpStatusCode.OK, body)
                } else
                    call.respond(
                        HttpStatusCode.fromValue(res.code()),
                        json.parseToJsonElement(res.errorBody()?.string()!!)
                    )
            }


            get("/list") {
                val token = call.request.headers["Authorization"]
                token?.let {
                    val res = async(Dispatchers.IO) {
                        client.getAllUsers(token)
                    }.await()
                    val body = res.body()

                    if (res.isSuccessful && body != null) {
                        call.respond(HttpStatusCode.OK, body)
                    } else
                        call.respond(
                            HttpStatusCode.fromValue(res.code()),
                            json.parseToJsonElement(res.errorBody()?.string()!!)
                        )
                } ?: run {
                    call.respond(
                        HttpStatusCode.Unauthorized
                    )
                }
            }

            get("/me") {
                val token = call.request.headers["Authorization"]
                token?.let {
                    val res = async(Dispatchers.IO) {
                        client.getUserMe(token)
                    }.await()
                    val body = res.body()

                    if (res.isSuccessful && body != null) {
                        call.respond(HttpStatusCode.OK, body)
                    } else
                        call.respond(
                            HttpStatusCode.fromValue(res.code()),
                            json.parseToJsonElement(res.errorBody()?.string()!!)
                        )
                } ?: run {
                    call.respond(
                        HttpStatusCode.Unauthorized
                    )
                }
            }
            patch("/me") {
                val token = call.request.headers["Authorization"]
                val multipart = call.receiveMultipart().readPart() as PartData.FileItem
                val requestBody = RequestBody.create(
                    MediaType.parse(multipart.contentType.toString()),
                    multipart.streamProvider().readBytes()
                )
                val multipartBody = MultipartBody.Part.createFormData("file", multipart.originalFileName, requestBody)
                val myScope = CoroutineScope(Dispatchers.IO)

                val res = myScope.async { client.updateAvatarUsuario(token!!, multipartBody) }.await()
                val body = res.body()
                if (res.isSuccessful && body != null) {
                    call.respond(HttpStatusCode.Created, body)
                } else call.respond(
                    HttpStatusCode.fromValue(res.code()),
                    json.parseToJsonElement(res.errorBody()?.string()!!)
                )

            }


            authenticate {



            }
        }
    }
}