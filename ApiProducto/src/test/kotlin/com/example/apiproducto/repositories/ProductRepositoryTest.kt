package com.example.apiproducto.repositories

import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest

@ExperimentalCoroutinesApi
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProductRepositoryTest {
    @Autowired
    lateinit var repository: ProductRepository

    private val product =Product(name="Test", price = 2.50, available=true,
        description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
        brand = "marca", model = "model")


    @Test
    fun save() = runTest{
        val save = repository.save(product)
        assertAll(
            {assertEquals(save.name, product.name)},
            {assertEquals(save.price, product.price)},
            {assertEquals(save.available, product.available)},
            {assertEquals(save.description, product.description)},
            {assertEquals(save.url, product.url)},
            {assertEquals(save.category, product.category)},
            {assertEquals(save.brand, product.brand)},
            {assertEquals(save.model, product.model)}
        )
    }

    @Test
    fun findAll() = runTest {
        repository.save(product)
        val all = repository.findAll().toList()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            {assertEquals(all[0].name, product.name)},
            {assertEquals(all[0].price, product.price)},
            {assertEquals(all[0].available, product.available)},
            {assertEquals(all[0].description, product.description)},
            {assertEquals(all[0].url, product.url)},
            {assertEquals(all[0].category, product.category)},
            {assertEquals(all[0].brand, product.brand)},
            {assertEquals(all[0].model, product.model)}
        )
    }

    @Test
    fun findByUuid() = runTest {
        repository.save(product)
        val find = repository.findProductByUuid(product.uuid)
        val block = withContext(Dispatchers.IO) {
            find.block()
        }

        assertAll(
            {assertTrue(block!=null)},
            {assertEquals(block?.name, product.name)},
            {assertEquals(block?.price, product.price)},
            {assertEquals(block?.available, product.available)},
            {assertEquals(block?.description, product.description)},
            {assertEquals(block?.url, product.url)},
            {assertEquals(block?.category, product.category)},
            {assertEquals(block?.brand, product.brand)},
            {assertEquals(block?.model, product.model)}
        )
    }

//    @Test
//    fun delete() = runTest {
//        repository.save(product)
//        val block = withContext(Dispatchers.IO) {
//            val find = repository.findProductByUuid(product.uuid)
//            find.block()
//        }
//        repository.delete(block!!)
//
//    }
}