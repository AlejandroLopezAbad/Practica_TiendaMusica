package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ServiceBadRequestException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.mappers.toService
import com.example.apiproducto.mappers.toServiceDto
import com.example.apiproducto.models.Service
import com.example.apiproducto.services.ServicesService
import com.example.apiproducto.services.TokenService
import com.example.apiproducto.validators.validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/service")
class ServiceController
@Autowired constructor(
    private val service: ServicesService,
    private val tokenService: TokenService,
) {
    @GetMapping("/prueba")
    fun prueba(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<Void> {
        try {
            val roles = getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) println("Muestra todo por defecto")
            else println("Muestra la otra")
        } catch (e: InvalidTokenException) {
            println(e.message)
        }
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    suspend fun getAllServices(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<List<Any>> {
        return try {
            val roles = getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) {
                val res = service.findAllServices().toList()
                ResponseEntity.ok(res)
            } else {
                val res = service.findAllServices().toList().filter { it.available }.map { it.toServiceDto() }
                ResponseEntity.ok(res)
            }
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/{uuid}")
    suspend fun findByUuid(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable uuid: String,
    ): ResponseEntity<Any> {
        return try {
            val roles = getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) {
                val res = service.findServiceByUuid(uuid)
                ResponseEntity.ok(res)
            } else {
                val res = service.findServiceByUuid(uuid)
                ResponseEntity.ok(res.toServiceDto())
            }
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping
    suspend fun saveService(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestBody service: ServiceCreateDto,
    ): ResponseEntity<Service> {
        try {
            val roles = getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) {
                service.validate()
                val res = this.service.saveService(service.toService())
                return ResponseEntity.status(HttpStatus.CREATED).body(res)
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("/{id}")
    suspend fun update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable id: Int,
        @RequestBody service: ServiceUpdateDto,
    ): ResponseEntity<Service> {
        try {
            val roles = getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) {
                val find = this.service.findServiceById(id)
                service.validate()
                val res = this.service.updateService(find, service)
                return ResponseEntity.ok(res)
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable id: Int,
    ): ResponseEntity<Service> {
        return try {
            val roles = getRoles(token)
            if (roles.contains("SUPERADMIN")) {
                this.service.deleteService(id)
                ResponseEntity.noContent().build()
            } else if (roles.contains("ADMIN")) {
                println("Soy admin")
                this.service.notAvailableService(id)
                ResponseEntity.noContent().build()
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


    private fun getRoles(token: String): String {
        return tokenService.getRoles(token)
    }
}