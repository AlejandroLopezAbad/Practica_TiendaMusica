package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.dto.UsersCreateDto
import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.dto.UsersWithTokenDto
import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.UsersServices
import com.example.microserviciousuarios.validators.validate
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(APIConfig.API_PATH + "/users")
class UsuarioController
@Autowired constructor(
    private val usersService: UsersServices,
){
   // @PostMapping("/login") seguridad y jwt

 /*  @PostMapping("/register")
    suspend fun register(@RequestBody usersCreateDto: UsersCreateDto):ResponseEntity<UsersWithTokenDto>{
        logger.info { "Registro de usuario: ${usersCreateDto.name}" }
        try {
           val user = usersCreateDto.validate()  //Hay que validarlo

            println(user.rol)

            val userSaved = usersService.save()//TODO ARREGLAR



        }


    }*/

    @GetMapping("/listUsers")
    suspend fun list( user: Users): ResponseEntity<List<UsersDto>> {

        logger.info { "Obteniendo lista de usuarios" }

        val res = usersService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }

    //Poner los permisos de preAuthorize
    @GetMapping("/me")
    fun meInfo(user: Users): ResponseEntity<UsersDto> {

        logger.info { "Obteniendo usuario: ${user.name}" }

        return ResponseEntity.ok(user.toDto())
    }


}