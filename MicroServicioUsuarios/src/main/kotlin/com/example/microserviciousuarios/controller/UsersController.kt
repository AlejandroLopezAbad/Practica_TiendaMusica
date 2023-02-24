package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil

import com.example.microserviciousuarios.dto.UsersCreateDto
import com.example.microserviciousuarios.dto.UsersDto
import com.example.microserviciousuarios.dto.UsersUpdateDto
import com.example.microserviciousuarios.dto.UsersWithTokenDto
import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.UsersServices
import com.example.microserviciousuarios.validators.validate
import jakarta.validation.Valid

import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.annotation.AuthenticationPrincipal

import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(APIConfig.API_PATH + "/users")
class UsuarioController
@Autowired constructor(
    private val usersService: UsersServices,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,

) {

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

    @GetMapping("/list") ///TODO METER user dentro del parametro para seguridad
    suspend fun list(): ResponseEntity<List<UsersDto>> {

        logger.info { "Obteniendo lista de usuarios" }

        val res = usersService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }

    //Poner los permisos de preAuthorize
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me") ///TODO METER EL USUARIO
    fun meInfo(@AuthenticationPrincipal user: Users): ResponseEntity<UsersDto> {

        logger.info { "Obteniendo usuario: ${user.name}" }

        return ResponseEntity.ok(user.toDto())
    }


    @PutMapping("/me")
    suspend fun updateMe(
        user: Users, //todo SEGURIDAD
        @Valid @RequestBody usersDto: UsersUpdateDto
    ): ResponseEntity<UsersDto> {
        // No hay que buscar porque el usuario ya está autenticado y lo tenemos en el contexto
        logger.info { "Actualizando usuario: ${user.name}" }

        usersDto.validate()

        val userUpdated = user.copy(
            email= usersDto.email,
            telephone = usersDto.telephone.toInt(),
            password = usersDto.password,
            url=usersDto.url
        )

        // Actualizamos el usuario
        try {
            val userUpdated = usersService.update(userUpdated)
            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: Exception) { ///BAD REQUES EXCEPTION
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


}