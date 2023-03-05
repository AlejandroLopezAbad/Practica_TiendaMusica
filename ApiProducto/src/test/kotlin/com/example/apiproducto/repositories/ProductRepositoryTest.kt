package com.example.apiproducto.repositories

import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@ExperimentalCoroutinesApi
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProductRepositoryTest {
    @Autowired
    lateinit var repository: ProductRepository

    @Test
    fun findAll() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val save = repository.save(product)
        val result = repository.findAll().toList()

        assertAll(
            { assertNotNull(result) },
            { assertEquals(product.name, result[0].name) },
            { assertEquals(product.price, result[0].price) },
            { assertEquals(product.available, result[0].available) },
            { assertEquals(product.description, result[0].description) },
            { assertEquals(product.url, result[0].url) },
            { assertEquals(product.category, result[0].category) },
            { assertEquals(product.stock, result[0].stock) },
            { assertEquals(product.brand, result[0].brand) },
            { assertEquals(product.model, result[0].model) }
        )

        repository.delete(save)
    }

    @Test
    fun findByUuid() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val result = repository.save(product)
        val find = repository.findProductByUuid(result.uuid).firstOrNull()

        assertAll(
            { assertNotNull(find) },
            { assertEquals(result.id, find?.id) },
            { assertEquals(product.uuid, find?.uuid) },
            { assertEquals(product.name, find?.name) },
            { assertEquals(product.price, find?.price) },
            { assertEquals(product.available, find?.available) },
            { assertEquals(product.description, find?.description) },
            { assertEquals(product.url, find?.url) },
            { assertEquals(product.category, find?.category) },
            { assertEquals(product.stock, find?.stock) },
            { assertEquals(product.brand, find?.brand) },
            { assertEquals(product.model, find?.model) }
        )

        repository.delete(result)
    }

    @Test
    fun findByUuidNotExist() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val block = repository.findProductByUuid(product.uuid).firstOrNull()
        assertNull(block)
    }

    @Test
    fun save() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val result = repository.save(product)

        assertAll(
            { assertEquals(product.uuid, result.uuid) },
            { assertEquals(product.name, result.name) },
            { assertEquals(product.price, result.price) },
            { assertEquals(product.available, result.available) },
            { assertEquals(product.description, result.description) },
            { assertEquals(product.url, result.url) },
            { assertEquals(product.category, result.category) },
            { assertEquals(product.stock, result.stock) },
            { assertEquals(product.brand, result.brand) },
            { assertEquals(product.model, result.model) },
        )

        repository.delete(result)
    }

    @Test
    fun update() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val result = repository.save(product)

        // Comprobamos que el resultado es correcto
        assertAll(
            { assertEquals(result.id, result.id) },
            { assertEquals(product.uuid, result.uuid) },
            { assertEquals(product.name, result.name) },
            { assertEquals(product.price, result.price) },
            { assertEquals(product.available, result.available) },
            { assertEquals(product.description, result.description) },
            { assertEquals(product.url, result.url) },
            { assertEquals(product.category, result.category) },
            { assertEquals(product.stock, result.stock) },
            { assertEquals(product.brand, result.brand) },
            { assertEquals(product.model, result.model) },
        )
        repository.delete(result)
    }

    @Test
    fun findById() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val save = repository.save(product)
        val find = repository.findById(save.id!!)

        assertAll(
            { assertEquals(save.id, find?.id) },
            { assertEquals(product.uuid, find?.uuid) },
            { assertEquals(product.name, find?.name) },
            { assertEquals(product.price, find?.price) },
            { assertEquals(product.available, find?.available) },
            { assertEquals(product.description, find?.description) },
            { assertEquals(product.url, find?.url) },
            { assertEquals(product.category, find?.category) },
            { assertEquals(product.stock, find?.stock) },
            { assertEquals(product.brand, find?.brand) },
            { assertEquals(product.model, find?.model) },
        )
        repository.delete(save)
    }

    @Test
    fun delete() = runTest {
        val product =Product(name="Test", price = 2.50, available=true,
            description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
            brand = "marca", model = "model")
        val result = repository.save(product)
        repository.delete(result)
    }
}