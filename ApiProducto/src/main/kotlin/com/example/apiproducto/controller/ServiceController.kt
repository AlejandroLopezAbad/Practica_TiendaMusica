package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.exceptions.ServiceException
import com.example.apiproducto.mappers.toService
import com.example.apiproducto.models.Service
import com.example.apiproducto.services.ServicesService
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/service")
class ServiceController
@Autowired constructor(
    private val service: ServicesService,
) {
    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<Service>> {
        val res = service.findAllServices().toList()
        return ResponseEntity.ok(res)
    }

    @PostMapping("")
    suspend fun create(@RequestBody service: ServiceCreateDto): ResponseEntity<Service> {
        val res = this.service.saveService(service.toService())
        return ResponseEntity.status(HttpStatus.CREATED).body(res)
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: Int): ResponseEntity<Service> {
        val res = service.findById(id)
        res?.let {
            return ResponseEntity.ok(it)
        } ?: run {
            return ResponseEntity.notFound().build()
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
    suspend fun update(@PathVariable id: Int, @RequestBody service: Service): ResponseEntity<Service> {
        val find = this.service.findById(id)
        find?.let {
            val res = this.service.updateService(it, service)
            return ResponseEntity.ok(res)
        } ?: run {
            return ResponseEntity.notFound().build()
        }
    }
}