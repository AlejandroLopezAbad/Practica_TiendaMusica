package com.example.routes

import com.example.models.*
import com.example.service.retrofit.RetroFitRest
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.patch
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
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
            post("/register", {
                description = "Registra un usuario "
                response {
                    default {
                        description =
                            "Da de alta un usuario con su nombre, email, telefono, contraseña y un url para su imagen que puede estar vacío "
                    }
                    HttpStatusCode.OK to {
                        description = "Usuario creado y su token JWT"
                        body<UserTokenDto> {
                            description = "Los datos del usuario creado y su token de acceso JWT"
                        }
                    }
                    HttpStatusCode.BadRequest to {
                        description = "El usuario no ha podido registrarse"
                    }
                }
            }) {
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

            post("/login", {
                description = "Logea un usuario"
                response {
                    default {
                        description =
                            "Inicia sesión usando la contraseña y el email del usuario "
                    }
                    HttpStatusCode.OK to {
                        description = "Usuario logeado y su token JWT"
                        body<UserTokenDto> {
                            description = "Los datos del usuario logeado y su token de acceso JWT"
                        }
                    }
                    HttpStatusCode.BadRequest to {
                        description = "El usuario no ha podido registrarse"
                    }
                    HttpStatusCode.NotFound to {
                        description = "El usuario no existe"
                    }
                }
            }) {
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


            get("/list", {
                description = "Lista todos los usuarios del sistema"
                response {
                    default {
                        description =
                            "Muestra todos los usuarios del sistema."
                    }
                    HttpStatusCode.OK to {
                        description = "Una lista con los usuarios del sistema"
                        body<List<UserDto>> {
                            description = "Los datos del usuario logeado y su token de acceso JWT"
                        }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "El token no es valido o ha caducado o no se tienen permisos para listar usuarios"
                    }
                    HttpStatusCode.BadRequest to {
                        description = "No se pudo consultar los usuarios"
                    }
                }
            }) {
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

            get("/me",{
                description = "Muestra los datos del usuario"
                response {
                    default {
                        description =
                            "Muestra los datos del usuario que está atutenticado con el token dado."
                    }
                    HttpStatusCode.OK to {
                        description = "El usuario logeado"
                        body<UserDto> {
                            description = "Los datos del usuario logeado al que pertenece el token de acceso"
                        }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "El token no es valido o ha caducado o no se tienen permisos para listar usuarios"
                    }
                }
            }) {
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
            patch("/me",{
                description = "Actualiza le avatar de un usuario"
                response {
                    default {
                        description =
                            "Actualiza la imagen de perfil de un usuario por una dada."
                    }
                    HttpStatusCode.Created to {
                        description = "La imagen de perfil ha sido actualizada"
                        body<UserDto> {
                            description = "Los datos del usuario con su nueva imagen"
                        }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "El token no es valido o ha caducado o no se tienen permisos para listar usuarios"
                    }
                }
            }) {
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

        }
    }
}