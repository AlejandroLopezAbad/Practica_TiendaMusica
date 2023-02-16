package com.example.apiproducto.controller

import com.example.apiproducto.models.Service
import com.example.apiproducto.repositories.ServiceRepository
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
    private val serviceRepository: ServiceRepository,
) {
    @PostMapping("")
    suspend fun create(@RequestBody service: Service): ResponseEntity<Service> {
        val res = serviceRepository.save(service)
        return ResponseEntity.status(HttpStatus.CREATED).body(res)
    }

    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<Service>> {
        val res = serviceRepository.findAll().toList()
        return ResponseEntity.ok(res)
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<Service> {
        val res = serviceRepository.findById(id.toInt())
        return ResponseEntity.ok(res)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Service> {
        try {
            serviceRepository.deleteById(id.toInt())
        } catch (e: Exception) {
            println("Mal")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "esat mal")
        }
        return ResponseEntity.noContent().build()
    }

    // TODO hay que mejorarlo para q busque por id
    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, @RequestBody service: Service): ResponseEntity<Service> {
        val res = serviceRepository.save(service)
        return ResponseEntity.status(HttpStatus.CREATED).body(res)
    }
}