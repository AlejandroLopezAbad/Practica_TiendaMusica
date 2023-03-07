package com.example.routes

import com.example.models.*
import com.example.service.retrofit.RetroFitRest
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
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
import org.koin.ktor.ext.inject
import retrofit2.HttpException

fun Application.serviciosRoutes(){
    val client : RetroFitRest by inject(qualifier = named("apiProduct"))
    val json = Json { prettyPrint=true }

    routing {
        route("/service"){

            get({
                description = "Conseguir todos los servicios"
                response {
                    default {
                        description = "Lista con todos los servicios"
                    }
                    HttpStatusCode.OK to {
                        description = "Lista de servicios"
                        body<List<Service>> { description = "Lista de todos los servicios" }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Token inválido"
                    }
                }
            }) {
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


            get("/{id}", {
                description = "Encontrar Servicio por id"
                request {
                    pathParameter<String>("id") {
                        description = "UUID del servicio a buscar"
                        required = true
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Servicio con el id indicado"
                        body<ProductResponseDto> { description = "Servicio encontrado" }
                    }
                    HttpStatusCode.NotFound to {
                        description = "No se ha encontrado el Servicio"
                        body<String> { description = "El id no existe" }
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "El token no es válido"
                    }
                }
            }) {
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

                post({
                    description = "Insertar un Servicio"
                    request {
                        body<ServiceCreateDto> {
                            description = "Datos del servicio que queremos crear"
                        }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Servicio añadido"
                            body<ProductResponseDto> { description = "Servicio que ha sido creado" }
                        }
                        HttpStatusCode.BadRequest to {
                            description = "Validaciones del servicio incorrectas"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes los permisos para realizar el insertado"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val service = call.receive<ServiceCreateDto>()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.creteService(token,service) }.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }

                delete("/{id}", {
                    description = "Eliminar un servicio"
                    request {
                        pathParameter<String>("id"){
                            description = "UUID del Servicio que queremos eliminar"
                            required = true
                        }
                    }
                    response{
                        HttpStatusCode.NoContent to {
                            description="El Servicio ha sido eliminado correctamente"
                        }
                        HttpStatusCode.NotFound to {
                            description = "No se ha encontrado el servicio con ese id"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes los permisos para realizar la eliminación"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val uuid = call.parameters["id"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteService(uuid,token) }.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }

                put("/{id}", {
                    description = "Actualizar un servicio"
                    request {
                        pathParameter<String>("id") {
                            description = "UUID del servicio a actualizar"
                            required = true
                        }
                        body<ServiceUpdateDto> {
                            description = "Datos del servicio a actualizar"
                        }
                    }
                    response {
                        HttpStatusCode.OK to{
                            description = "Servicio actualizado correctamente"
                            body<ProductResponseDto> {description = "Servicio actualizado"}
                        }
                        HttpStatusCode.NotFound to {
                            description= "No se ha encontrado el servicio con el id indicado"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="Validación de los datos inválida"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la actualización"
                        }
                    }
                }) {
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

        route("/storage/service"){
            authenticate {

                post("/{uuid}", {
                    description = "Insertar una imagen a un servicio"
                    request {
                        pathParameter<String>("id") {
                            description = "UUID del servicio a actualizar la imagen"
                            required = true
                        }
                        multipartBody {
                            description = "Imagen a poner en el servicio"
                        }
                    }
                    response {
                        HttpStatusCode.Created to{
                            description = "Imagen creada correctamente"
                        }
                        HttpStatusCode.NotFound to {
                            description= "No se ha encontrado el servicio con el id indicado"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="No se puede almacenar ficheros vacios"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val multipart = call.receiveMultipart().readPart() as PartData.FileItem
                    val uuid = call.parameters["uuid"].toString()

                    val requestBody = RequestBody.create(MediaType.parse(multipart.contentType.toString()),multipart.streamProvider().readBytes())
                    val multipartBody = MultipartBody.Part.createFormData("file",multipart.originalFileName,requestBody)

                    val myScope = CoroutineScope(Dispatchers.IO)

                    val res = myScope.async { client.saveFileService(uuid,token,multipartBody)}.await()
                    val body = res.body()
                    if (res.isSuccessful && body != null){
                        call.respond(HttpStatusCode.Created,body)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }


                get("/{filename}", {
                    description = "Pedir una imagen a un servicio"
                    request {
                        pathParameter<String>("filename") {
                            description = "nombre y extensión de la imagen que buscamos"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to{
                            description = "Imagen encontrada correctamente"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="No se puede determinar el tipo del fichero"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val filename = call.parameters["filename"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    try {
                        val res = myScope.async { client.getFileService(filename,token)}.await()
                        call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=$filename")
                        call.respondBytes(res.bytes())
                    }catch (e: HttpException){
                        call.respond(HttpStatusCode.fromValue(e.code()), json.parseToJsonElement(e.response()?.errorBody()?.string()!!))
                    }

                }


                delete("/{filename}", {
                    description = "Eliminar una imagen"
                    request {
                        pathParameter<String>("filename") {
                            description = "nombre y extensión de la imagen que buscamos"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to{
                            description = "Imagen eliminada correctamente"
                        }
                        HttpStatusCode.BadRequest to{
                            description ="Problemas con el storage"
                        }
                        HttpStatusCode.Unauthorized to {
                            description = "No tienes permisos para realizar la acción"
                        }
                    }
                }) {
                    val token = call.request.headers["Authorization"]?.replace("Bearer ", "").toString()
                    val filename = call.parameters["filename"].toString()

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val res = myScope.async { client.deleteFileService(filename,token)}.await()
                    if (res.isSuccessful){
                        call.respond(HttpStatusCode.NoContent)
                    }else call.respond(HttpStatusCode.fromValue(res.code()), json.parseToJsonElement(res.errorBody()?.string()!!))
                }
            }
        }
    }
}