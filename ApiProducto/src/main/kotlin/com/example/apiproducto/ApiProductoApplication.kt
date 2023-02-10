package com.example.apiproducto

import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.repositories.ProductRepository
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
class ApiProductoApplication
@Autowired constructor(
    var repo: ProductRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        var product = Product(name="Prueba", price = 2.20, available= true, description = "Esto es una descripcion"
            , url="", category = ProductCategory.BOOSTER, stock = 10, brand = "Marca", model = "modelo", id = 1234567)
        var add = repo.save(product)
        var result = add.block()
        print(result)
    }

}

fun main(args: Array<String>) {
    runApplication<ApiProductoApplication>(*args)
}
