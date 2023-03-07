package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ServiceBadRequestException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.exceptions.StorageBadRequestException
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
    @GetMapping
    suspend fun getAllServices(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String?): ResponseEntity<List<Any>> {
        return try {
            token?.let {
                val roles = tokenService.getRoles(it)
                if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")) {
                    val res = service.findAllServices().toList()
                    return ResponseEntity.ok(res)
                } else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            } ?: run {
                val res = service.findAllServices().toList().filter { it.available }.map { it.toServiceDto() }
                ResponseEntity.ok(res)
            }
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }

    @GetMapping("/{uuid}")
    suspend fun findByUuid(
        @PathVariable uuid: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String?,
    ): ResponseEntity<Any> {
        return try {
            token?.let {
                val roles = tokenService.getRoles(token)
                if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")) {
                    val res = service.findServiceByUuid(uuid)
                    ResponseEntity.ok(res)
                } else throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no es válido.")
            } ?: run {
                val res = service.findServiceByUuid(uuid)
                if (res.available) ResponseEntity.ok(res.toServiceDto()) else throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe el servicio."
                )
            }
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }

    @PostMapping
    suspend fun saveService(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestBody service: ServiceCreateDto,
    ): ResponseEntity<Service> {
        try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN") || roles.contains("EMPLOYEE")) {
                service.validate()
                val res = this.service.saveService(service.toService())
                return ResponseEntity.status(HttpStatus.CREATED).body(res)
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }

    @PutMapping("/{uuid}")
    suspend fun update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable uuid: String,
        @RequestBody service: ServiceUpdateDto,
    ): ResponseEntity<Service> {
        try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("ADMIN") || roles.contains("SUPERADMIN")) {
                val find = this.service.findServiceByUuid(uuid)
                service.validate()
                val res = this.service.updateService(find, service)
                return ResponseEntity.ok(res)
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        }
    }

    @DeleteMapping("/{uuid}")
    suspend fun delete(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @PathVariable uuid: String,
    ): ResponseEntity<Service> {
        return try {
            val roles = tokenService.getRoles(token)
            if (roles.contains("SUPERADMIN")) {
                this.service.deleteService(uuid)
                ResponseEntity.noContent().build()
            } else if (roles.contains("ADMIN")) {
                this.service.notAvailableService(uuid)
                ResponseEntity.noContent().build()
            } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esto.")
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidTokenException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
        } catch (e: StorageBadRequestException){
            throw  ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}