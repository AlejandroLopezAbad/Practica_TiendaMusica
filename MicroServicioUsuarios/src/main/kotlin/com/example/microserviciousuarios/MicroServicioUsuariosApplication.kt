package com.example.microserviciousuarios

import com.example.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroServicioUsuariosApplication : CommandLineRunner {
    @Autowired
    lateinit var service: UsersRepository
    override fun run(vararg args: String?) = runBlocking {

    val prueba = service.findAll().toList()

        println(prueba)

    }
}

fun main(args: Array<String>) {
    runApplication<MicroServicioUsuariosApplication>(*args)
}
