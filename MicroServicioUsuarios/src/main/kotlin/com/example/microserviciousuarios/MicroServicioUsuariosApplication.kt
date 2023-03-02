package com.example.microserviciousuarios

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class MicroServicioUsuariosApplication/*(
    @Autowired
    private var service: UsersServices
) : CommandLineRunner {
    override fun run(vararg args: String?): Unit = runBlocking {
        val user = Users(
            email= "ekix@gmail.com",
            name= "ekix 2.0",
            password = "\$2a\$12\$tZ9OjqIhDYxZj3cZmHWmmulKtxql.DvpCnxyA/OOYaOXbKH55Jrra",//ekix1234
            telephone = 896655442,
            rol = Users.TypeRol.ADMIN.name,
            avaliable = true,
            url = "fotazo")

        service.update(user)


        println(user)


       // println(user)

       // val user =UsersUpdateDto("alex@alex.com","Alex")

      //  var help =service.update()

        var ayuda = service.findAll().toList()
        println(ayuda)


//        val prueba = service.findAll().collect{
//            println(it)
//        }
        //TODO CAMBIAR EL METER USUARIOS Y USAR EL SCRIP, funciona el login y el list ocn permisos de admin
    }
}*/

fun main(args: Array<String>) {
    runApplication<MicroServicioUsuariosApplication>(*args)
}
