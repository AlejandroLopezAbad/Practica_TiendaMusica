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
            email= "Alex@prueba.com",
            name= "Pepe",
            password = "\$2a\$12\$249dkPGBT6dH46f4Dbu7ouEuO8eZ7joonzWGefPJbHH8eDpJy0oCq",
            telephone = 787744552,
            rol = Users.TypeRol.ADMIN.name,
            avaliable = true,
            url = "url")
        service.save(user)



        println(user)

        var ayuda = service.findAll().toList()
        println(ayuda)


//        val prueba = service.findAll().collect{
//            println(it)
//        }
        //TODO CAMBIAR EL METER USUARIOS Y USAR EL SCRIP, funciona el login y el list ocn permisos de admin
    }
}

fun main(args: Array<String>) {
    runApplication<MicroServicioUsuariosApplication>(*args)
}
