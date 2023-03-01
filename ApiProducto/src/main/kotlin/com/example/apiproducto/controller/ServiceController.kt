package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceDto
import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ServiceBadRequestException
import com.example.apiproducto.exceptions.ServiceException
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
            tokenService.tokenVerify(token)
            val roles = tokenService.getRoles(token)
            println(roles)
        } catch (e: InvalidTokenException) {
            println(e.message)
        }
        return ResponseEntity.noContent().build()
    }

    @GetMapping("")
    suspend fun getAllServices(): ResponseEntity<List<ServiceDto>> {
        val res = service.findAllServices().toList().map { it.toServiceDto() }
        return ResponseEntity.ok(res)
    }

    @PostMapping("")
    suspend fun saveService(@RequestBody service: ServiceCreateDto): ResponseEntity<Service> {
        try {
            service.validate()
            val res = this.service.saveService(service.toService())
            return ResponseEntity.status(HttpStatus.CREATED).body(res)
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

//    @GetMapping("/{id}")
//    suspend fun findById(@PathVariable id: Int): ResponseEntity<Service> {
//        try {
//            val find = service.findServiceById(id)
//            return ResponseEntity.ok(find)
//        } catch (e: ServiceNotFoundException) {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
//        }
//    }

    @GetMapping("/{uuid}")
    suspend fun findByUuid(@PathVariable uuid: String): ResponseEntity<Service> {
        try {
            val res = service.findServiceByUuid(uuid)
            return ResponseEntity.ok(res)
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: Int): ResponseEntity<Service> {
        try {
            this.service.deleteService(id)
            return ResponseEntity.noContent().build()
        } catch (e: ServiceException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: Int, @RequestBody service: ServiceUpdateDto): ResponseEntity<Service> {
        try {
            val find = this.service.findServiceById(id)
            service.validate()
            val res = this.service.updateService(find, service)
            return ResponseEntity.ok(res)
        } catch (e: ServiceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: ServiceBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}