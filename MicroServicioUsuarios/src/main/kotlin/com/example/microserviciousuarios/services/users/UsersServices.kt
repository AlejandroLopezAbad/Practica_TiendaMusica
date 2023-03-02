package com.example.microserviciousuarios.services.users

import com.example.microserviciousuarios.exceptions.UsersBadRequestException
import com.example.microserviciousuarios.exceptions.UsersNotFoundException
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * Servicio que realiza operaciones de usuarios.
 */
@Service
class UsersServices
@Autowired constructor(
    private val repository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
): UserDetailsService {


    override fun loadUserByUsername(email: String): UserDetails = runBlocking {
        return@runBlocking repository.findByEmail(email).firstOrNull()
            ?: throw UsersNotFoundException("Usuario no encontrado con username: $email")
    }



    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }


    suspend fun loadUserById(userId: Long) = withContext(Dispatchers.IO) {
        return@withContext repository.findById(userId)
    }

    suspend fun loadUserbyUuid(uuid:String)= withContext(Dispatchers.IO) {
        return@withContext repository.findByUuid(uuid).firstOrNull()
    }

    suspend fun save(user: Users, isAdmin: Boolean = false): Users = withContext(Dispatchers.IO) {

        logger.info { "Guardando usuario: $user" }

        if (repository.findByEmail(user.email).firstOrNull() != null) {

            logger.info { "El usuario ya existe con este email" }
            throw UsersBadRequestException("El usuario ya existe con este email")
        }
        if (repository.findByTelephone(user.telephone).firstOrNull() != null) {

            logger.info { "El usuario ya existe con este numero de telefono " }
            throw UsersBadRequestException("El usuario ya existe con este numero de telefono ")
        }


        logger.info { "El usuario no esta registrado , lo guardamos" }
        var newUser = user.copy(
            uuid = UUID.randomUUID().toString(),
            password = passwordEncoder.encode(user.password),
            rol = Users.TypeRol.USER.name,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()

        )
        if (isAdmin) {
            newUser = newUser.copy(
                rol = Users.TypeRol.ADMIN.name
            )
        }

        println(newUser)
        try {
            return@withContext repository.save(newUser)
        } catch (e: Exception) {
            throw UsersBadRequestException("Error al crear el usuario: Nombre de usuario o email ya existen")
        }

    }

    suspend fun update(user: Users) = withContext(Dispatchers.IO) {
        logger.info { "Actualizando usuario: $user" }

        var userDB = repository.findByName(user.name)
            .firstOrNull()

        if (userDB != null && userDB.id != user.id) {
            throw UsersBadRequestException("El Id ya existe")
        }

        userDB = repository.findByEmail(user.email!!)
            .firstOrNull()

        if (userDB != null && userDB.id != user.id) {
            throw UsersBadRequestException("El email ya existe")
        }

        logger.info { "El usuario no existe, lo actualizamos" }

        val updtatedUser = user.copy(
            updatedAt = LocalDateTime.now()
        )

        try {
            return@withContext repository.save(updtatedUser)
        } catch (e: Exception) {
            throw UsersBadRequestException("Error al actualizar el usuario: Nombre de usuario o email ya existen")
        }

    }
}