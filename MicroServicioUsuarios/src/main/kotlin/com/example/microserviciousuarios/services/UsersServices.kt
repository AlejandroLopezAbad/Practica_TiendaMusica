package com.example.microserviciousuarios.services

import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class UsersServices
@Autowired constructor(
    private val repository: UsersRepository
) {
    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }


    suspend fun loadUserById(userId: Long) = withContext(Dispatchers.IO) {
        return@withContext repository.findById(userId)
    }

    suspend fun save(user: Users, isAdmin: Boolean = false): Users = withContext(Dispatchers.IO) {

        logger.info { "Guardando usuario: $user" }
        /*
               if(repository.findByName(user.name).firstOrNull() !=null){
                    logger.info { "El usuario ya existe" }
                    throw Exception("EL nom")
                }
                //TODO Restriciciones
        */
        logger.info { "El usuario no esta registrado , lo guardamos" }
        var newUser = user.copy(
            uuid = UUID.randomUUID().toString(),
            password = user.password,
            rol = Users.TypeRol.USER,

            )
        if (isAdmin) { //TODO comprobar que funciona
            newUser = newUser.copy(
                rol = Users.TypeRol.ADMIN
            )
        }

        println(newUser)
        try {
            return@withContext repository.save(newUser)
        } catch (e: Exception) {
            throw Exception("Error al crear el usuario: Nombre de usuario o email ya existen")
        }

    }

    suspend fun update(user: Users) = withContext(Dispatchers.IO) {
        logger.info { "Actualizando usuario: $user" }


        var userDB = repository.findByName(user.name)
            .firstOrNull()

        if (userDB != null && userDB.id != user.id) {
            throw Exception("El Id ya existe")//TODO cambiar excepciones
        }

        userDB = repository.findByEmail(user.email)
            .firstOrNull()

        if (userDB != null && userDB.id != user.id) {
            throw Exception("El email ya existe")
        }

        logger.info { "El usuario no existe, lo actualizamos" }

        val updtatedUser = user.copy(
            updatedAt = LocalDateTime.now()
        )

        try {
            return@withContext repository.save(updtatedUser)
        } catch (e: Exception) {
            throw Exception("Error al actualizar el usuario: Nombre de usuario o email ya existen")
        }

    }
}