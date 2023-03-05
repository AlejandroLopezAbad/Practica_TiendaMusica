package com.example.apiproducto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ApiProductoApplication

fun main(args: Array<String>) {
    runApplication<ApiProductoApplication>(*args)
}
