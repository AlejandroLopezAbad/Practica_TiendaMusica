package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil
import com.example.microserviciousuarios.dto.*

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
        //  logger.info { "Login de usuario: ${logingDto.username}" }

        // podríamos hacerlo preguntándole al servicio si existe el usuario
        // pero mejor lo hacemos con el AuthenticationManager que es el que se encarga de ello
        // y nos devuelve el usuario autenticado o null

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
        // logger.info { "Token de usuario: ${jwtToken}" }

        // Devolvemos el usuario con el token
        val userWithToken = UsersWithTokenDto(user.toDto(), jwtToken)

        // La respuesta que queremos

        return ResponseEntity.ok(userWithToken)
    }

    /*  @PostMapping("/register")
       suspend fun register(@RequestBody usersCreateDto: UsersCreateDto):ResponseEntity<UsersWithTokenDto>{
           logger.info { "Registro de usuario: ${usersCreateDto.name}" }
           try {
              val user = usersCreateDto.validate()  //Hay que validarlo

               println(user.rol)

               val userSaved = usersService.save()//TODO ARREGLAR



           }


       }*/

    @PreAuthorize("hasRole('ADMIN')")
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