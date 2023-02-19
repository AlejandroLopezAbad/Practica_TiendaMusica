package com.example.microserviciousuarios

import com.example.microserviciousuarios.controller.UserController
import com.example.microserviciousuarios.models.RoleCategory
import com.example.microserviciousuarios.models.User
import com.example.microserviciousuarios.services.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class MicroServicioUsuariosApplication
@Autowired constructor(
    var usersController: UserController,
    var usersService: UserService
) : CommandLineRunner {
    override fun run(vararg args: String?): Unit = runBlocking {
        val userFran = User(1, UUID.randomUUID().toString(),"fran@fran.es","fran","1234",
            "666666666", RoleCategory.ADMIN,true,"fran.es")

        val usersList = mutableListOf<User>()
        usersList.add(userFran)

        println(usersList)

        usersService.saveUser(userFran)


    }

}

fun main(args: Array<String>) {
    runApplication<MicroServicioUsuariosApplication>(*args)
}
