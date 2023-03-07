package com.example.apiproducto.controller

import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.exceptions.StorageBadRequestException
import com.example.apiproducto.services.ProductService
import com.example.apiproducto.services.ServicesService
import com.example.apiproducto.services.TokenService
import com.example.apiproducto.services.storage.StorageService
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/storage")
class StorageController
@Autowired constructor(
    private val storageService: StorageService,
    private val productService: ProductService,
    private val servicesService: ServicesService,
    private val tokenService: TokenService,
) {

    @PostMapping(
        value = ["/product/{uuid}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun storeProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestPart("file") file: MultipartFile,
        @PathVariable uuid: String,
    ): ResponseEntity<Map<String, String>> = runBlocking {
        return@runBlocking try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")) {
                if (!file.isEmpty) {
                    val saved = storageService.storeProduct(file, uuid)
                    productService.changeUrlProduct(uuid, saved)
                    val response = mapOf("name" to saved, "created_at" to LocalDateTime.now().toString())
                    ResponseEntity.status(HttpStatus.CREATED).body(response)
                } else {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede almacenar un fichero vacío")
                }
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: StorageBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: ProductNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @PostMapping(
        value = ["/service/{uuid}"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun storeService(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestPart("file") file: MultipartFile,
        @PathVariable uuid: String,
    ): ResponseEntity<Map<String, String>> = runBlocking {
        return@runBlocking try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")) {
                if (!file.isEmpty) {
                    val saved = storageService.storeService(file, uuid)
                    servicesService.changeUrlService(uuid, saved)
                    val response = mapOf("name" to saved, "created_at" to LocalDateTime.now().toString())
                    ResponseEntity.status(HttpStatus.CREATED).body(response)
                } else {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede almacenar un fichero vacío")
                }
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: StorageBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }


    @GetMapping(value = ["/product/{filename}"])
    @ResponseBody
    fun getProductResource(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable filename: String?,
        request: HttpServletRequest,
    ): ResponseEntity<Resource> = runBlocking {
        val file: Resource = storageService.loadAsResource(filename.toString(), "PRODUCT")
        var contentType: String?
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (ex: IOException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo del fichero")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }


    @GetMapping(value = ["/service/{filename}"])
    @ResponseBody
    fun getServiceResource(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable filename: String?,
        request: HttpServletRequest,
    ): ResponseEntity<Resource> = runBlocking {
        val file: Resource = storageService.loadAsResource(filename.toString(), "SERVICE")
        var contentType: String?
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (ex: IOException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo del fichero")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }


    @DeleteMapping(value = ["/product/{filename}"])
    @ResponseBody
    fun deleteProductFile(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable filename: String?,
        request: HttpServletRequest,
    ): ResponseEntity<Resource> = runBlocking {
        return@runBlocking try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")) {
                storageService.deleteProduct(filename.toString())
                productService.deleteUrlProduct(filename.toString(), "placeholder.jpg")
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: StorageBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        } catch (e: ProductNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


    @DeleteMapping(value = ["/service/{filename}"])
    @ResponseBody
    fun deleteServiceFile(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable filename: String?,
        request: HttpServletRequest,
    ): ResponseEntity<Resource> = runBlocking {
        return@runBlocking try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLEADO")) {
                storageService.deleteService(filename.toString())
                servicesService.deleteUrlService(filename.toString(), "placeholder.jpg")
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para realizar esta acción")
            }
        } catch (e: StorageBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


}