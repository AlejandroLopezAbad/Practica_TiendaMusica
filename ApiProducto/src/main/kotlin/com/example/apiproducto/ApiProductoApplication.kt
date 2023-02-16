package com.example.apiproducto

import com.example.apiproducto.controller.ProductController
import com.example.apiproducto.controller.ServiceController
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.repositories.ProductRepository
import com.example.apiproducto.repositories.ServiceRepository
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiProductoApplication
@Autowired constructor(
    var services: ServiceController,
    var products: ProductController
)

fun main(args: Array<String>) {
    runApplication<ApiProductoApplication>(*args)
}
