package com.example.apiproducto.controller

import com.example.apiproducto.models.Service
import com.example.apiproducto.repositories.ServiceRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
}