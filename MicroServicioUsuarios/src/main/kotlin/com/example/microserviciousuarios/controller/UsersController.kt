package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil
import com.example.microserviciousuarios.dto.*
import com.example.microserviciousuarios.exceptions.UsersBadRequestException

import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.mappers.toModel
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder

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


    @PostMapping("/login")
    fun login(@Valid @RequestBody logingDto: UsersLoginDto): ResponseEntity<UsersWithTokenDto> {

        logger.info { "Login de usuario: ${logingDto.email}" }


        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                logingDto.email,
                logingDto.password
            )
        )
        // Autenticamos al usuario, si lo es nos lo devuelve
        SecurityContextHolder.getContext().authentication = authentication

        // Devolvemos al usuario autenticado
        val user = authentication.principal as Users
        println(user)

        // Generamos el token
        val jwtToken: String = jwtTokenUtil.generateToken(user)

        logger.info { "Token de usuario: ${jwtToken}" }

        // Devolvemos el usuario con el token
        val userWithToken = UsersWithTokenDto(user.toDto(), jwtToken)

        // La respuesta que queremos

        return ResponseEntity.ok(userWithToken)
    }

    @PostMapping("/register")
    suspend fun register(@RequestBody usersCreateDto: UsersCreateDto): ResponseEntity<UsersWithTokenDto> {
        logger.info { "Registro de usuario: ${usersCreateDto.name}" }
        try {
            val user = usersCreateDto.validate().toModel()  //Hay que validarlo

            user.rol.forEach { println(it) }

            val userSaved = usersService.save(user)

            //ahora generamos el token
            val jwtToken: String = jwtTokenUtil.generateToken(userSaved)
            logger.info { "Token de users : ${jwtToken} " }

            return ResponseEntity.ok(UsersWithTokenDto(userSaved.toDto(), jwtToken))

        } catch (e:UsersBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list") ///TODO METER user dentro del parametro para seguridad
    suspend fun list(): ResponseEntity<List<UsersDto>> {

        logger.info { "Obteniendo lista de usuarios" }

        val res = usersService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me") ///TODO METER EL USUARIO
    fun meInfo(@AuthenticationPrincipal user: Users): ResponseEntity<UsersDto> {

        logger.info { "Obteniendo usuario: ${user.name}" }

        return ResponseEntity.ok(user.toDto())
    }


    @PutMapping("/me")
    suspend fun updateMe(
        @AuthenticationPrincipal
        user: Users, //todo SEGURIDAD
        @Valid @RequestBody usersDto: UsersUpdateDto
    ): ResponseEntity<UsersDto> {
        // No hay que buscar porque el usuario ya está autenticado y lo tenemos en el contexto
        logger.info { "Actualizando usuario: ${user.name}" }

        usersDto.validate()

        val userUpdated = user.copy(

            email = usersDto.email,
            name=usersDto.name
           // telephone = usersDto.telephone.toInt(),
        )

        // Actualizamos el usuario
        try {
            val userUpdated = usersService.update(userUpdated)

            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: Exception) { ///BAD REQUES EXCEPTION
            println("no funciona")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


}