package com.example.apiproducto

import com.example.apiproducto.controller.ProductController
import com.example.apiproducto.controller.ServiceController
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ApiProductoApplication
@Autowired constructor(
    var services: ServiceController,
    var products: ProductController,
) : CommandLineRunner {
    override fun run(vararg args: String?): Unit = runBlocking {
//        val service = Service(
//            category = ServiceCategory.AMPLIFIER_REPAIR,
//            available = true,
//            price = 2.5,
//            description = "Descripción",
//            url = ""
//        )
//        services.create(service)
    }
}

fun main(args: Array<String>) {
    runApplication<ApiProductoApplication>(*args)
}
