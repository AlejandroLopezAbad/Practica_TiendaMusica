package com.example.apiproducto

import com.example.apiproducto.models.Service
import com.example.apiproducto.repositories.ServiceRepository
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiProductoApplication
@Autowired constructor(
    var repo: ServiceRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) = runBlocking {
        val product = Service(
            name = "Prueba",
            price = 2.20,
            available = true,
            description = "Esto es una descripcion",
            url = "",
            category = Service.ServiceCategory.GUITAR_REPAIR
        )
        val add = repo.save(product)
        println(add)
    }
}

fun main(args: Array<String>) {
    runApplication<ApiProductoApplication>(*args)
}
