package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.UsersServices
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(APIConfig.API_PATH + "/users")
class UsuarioController
@Autowired constructor(
    private val usersService: UsersServices,
){


    @GetMapping("/list")
    suspend fun list( user: Users): ResponseEntity<List<UsersDto>> {
        
        logger.info { "Obteniendo lista de usuarios" }

        val res = usersService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }
}