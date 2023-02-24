package com.example.microserviciousuarios

import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class MicroServicioUsuariosApplication(
    @Autowired
    private var service: UsersRepository
) : CommandLineRunner {
    override fun run(vararg args: String?): Unit = runBlocking {
        val user = Users(
            email= "prueba@prueba.com",
            name= "Profe",
            password = "1234",
            telephone = 787744552,
            rol = Users.TypeRol.USER.name,
            avaliable = true,
            url = "url")
        service.save(user)



        println(user)

        var ayuda = service.findAll().toList()
        println(ayuda)


//        val prueba = service.findAll().collect{
//            println(it)
//        }
    }
}

fun main(args: Array<String>) {
    runApplication<MicroServicioUsuariosApplication>(*args)
}
