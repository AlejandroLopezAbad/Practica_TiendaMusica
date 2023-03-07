package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.APIConfig
import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil
import com.example.microserviciousuarios.dto.*
import com.example.microserviciousuarios.exceptions.StorageException
import com.example.microserviciousuarios.exceptions.UsersBadRequestException

import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.mappers.toModel
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.storage.StorageService
import com.example.microserviciousuarios.services.users.UsersServices
import com.example.microserviciousuarios.validators.validate
import jakarta.validation.Valid

import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException


private val logger = KotlinLogging.logger {}

/**
 * Controlador que realiza las operaciones básicas de los usuarios.
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/users")
class UsuarioController
@Autowired constructor(
    private val usersService: UsersServices,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,
    private val storageService: StorageService
) {


    /**
     * Login
     *
     * @param logingDto
     * @return
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody logingDto: UsersLoginDto): ResponseEntity<UsersWithTokenDto> {

        logger.info { "Login:${logingDto.email}" }


        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                logingDto.email,
                logingDto.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as Users

        val jwtToken: String = jwtTokenUtil.generateToken(user)

        logger.info { "Token: ${jwtToken}" }

        val userWithToken = UsersWithTokenDto(user.toDto(), jwtToken)

        return ResponseEntity.ok(userWithToken)
    }

    /**
     * Register
     *
     * @param usersCreateDto
     * @return
     */
    @PostMapping("/register")
    suspend fun register(@RequestBody usersCreateDto: UsersCreateDto): ResponseEntity<UsersWithTokenDto> {
        logger.info { "Registrando al usuario: ${usersCreateDto.name}" }
        try {
            val user = usersCreateDto.validate().toModel()


            val userSaved = usersService.save(user)


            val jwtToken: String = jwtTokenUtil.generateToken(userSaved)
            logger.info { "Token del usuario : ${jwtToken} " }

            return ResponseEntity.ok(UsersWithTokenDto(userSaved.toDto(), jwtToken))

        } catch (e: UsersBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    /**
     * List
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN','SUPERADMIN')")
    @GetMapping("/list")
    suspend fun list(@AuthenticationPrincipal user: Users): ResponseEntity<List<UsersDto>> {

        logger.info { "Obteniendo lista de usuarios" }

        val res = usersService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }


    /**
     * Me info
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasAnyRole('USER','EMPLOYEE','ADMIN','SUPERADMIN')")
    @GetMapping("/me")
    fun meInfo(@AuthenticationPrincipal user: Users): ResponseEntity<UsersDto> {

        logger.info { "Obteniendo la informacíon  del usuario : ${user.name}" }

        return ResponseEntity.ok(user.toDto())
    }


    /**
     * Update me
     *
     * @param user
     * @param usersDto
     * @return
     */
    @PreAuthorize("hasAnyRole('USER','EMPLOYEE','ADMIN','SUPERADMIN')")
    @PutMapping("/me")
    suspend fun updateMe(
        @AuthenticationPrincipal
        user: Users,
        @Valid @RequestBody usersDto: UsersUpdateDto
    ): ResponseEntity<UsersDto> {
        logger.info { "Actualizando usuario: ${user.name}" }

        usersDto.validate()

        val userUpdated = user.copy(

            email = usersDto.email,
            name = usersDto.name,
            telephone = usersDto.telephone.toInt()
        )


        try {
            val userUpdated = usersService.update(userUpdated)

            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    /**
     * Update avatar
     *
     * @param user
     * @param file
     * @return
     */
    @PreAuthorize("hasAnyRole('USER','EMPLOYEE','ADMIN','SUPERADMIN')")
    @PatchMapping(
        value = ["/me"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    suspend fun updateAvatar(
        @AuthenticationPrincipal user: Users,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<UsersDto> {

        logger.info { "Actualizando avatar ${user.username}" }

        try {
            var urlImagen = user.url

            if (!file.isEmpty) {
                val imagen: String = storageService.save(file, user.uuid)
                urlImagen = storageService.getUrl(imagen)
            }
            val userAvatar = user.copy(
                url = urlImagen
            )
            val userUpdated = usersService.update(userAvatar)
            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: UsersBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: StorageException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}