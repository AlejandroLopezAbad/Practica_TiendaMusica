package com.example.apiproducto.services

import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.repositories.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ProductServiceTest {
    @MockK
    private lateinit var repository: ProductRepository
    @InjectMockKs
    private lateinit var service: ProductService

    private val test = Product(id=1, uuid= UUID.randomUUID().toString(), name="Test", price = 2.50, available=true,
        description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
        brand = "marca", model = "model")

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAllTest() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)
        val all = service.findAllProducts()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(test.name, all[0].name) },
            { assertEquals(test.price, all[0].price) },
            { assertEquals(test.available, all[0].available) },
            { assertEquals(test.description, all[0].description) },
            { assertEquals(test.url, all[0].url) },
            { assertEquals(test.category, all[0].category) },
            { assertEquals(test.stock, all[0].stock) },
            { assertEquals(test.brand, all[0].brand) },
            { assertEquals(test.model, all[0].model) }
        )
        coVerify(exactly =1){repository.findAll()}
    }

    @Test
    fun findByIdTest() = runTest{
        coEvery{ repository.findById(test.id!!)} returns test
        val find = service.findProductById(test.id!!)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.name, find.name) },
            { assertEquals(test.price, find.price) },
            { assertEquals(test.available, find.available) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.category, find.category) },
            { assertEquals(test.stock, find.stock) },
            { assertEquals(test.brand, find.brand) },
            { assertEquals(test.model, find.model) }
        )
        coVerify(exactly =1){repository.findById(test.id!!)}
    }

    @Test
    fun findByIdNotExistTest() = runTest{
        coEvery { repository.findById(test.id!!) } returns null
        val find = org.junit.jupiter.api.assertThrows<ProductNotFoundException> {
            service.findProductById(test.id!!)
        }
        assertEquals("No se ha encontrado un producto con el id: ${test.id}", find.message)
        coVerify(exactly=1){repository.findById(test.id!!)}
    }

    @Test
    fun findByUuidTest() = runTest{
        coEvery{ repository.findProductByUuid(test.uuid)} returns flowOf(test)
        val find = service.findProductByUuid(test.uuid)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.name, find.name) },
            { assertEquals(test.price, find.price) },
            { assertEquals(test.available, find.available) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.category, find.category) },
            { assertEquals(test.stock, find.stock) },
            { assertEquals(test.brand, find.brand) },
            { assertEquals(test.model, find.model) }
        )
        coVerify(exactly =1){repository.findProductByUuid(test.uuid)}
    }

    @Test
    fun findProductByUuidNotExistTest() = runTest{
        coEvery { repository.findProductByUuid(test.uuid) } returns flowOf()
        val find = org.junit.jupiter.api.assertThrows<ProductNotFoundException> {
            service.findProductByUuid(test.uuid)
        }
        assertEquals("No se ha encontrado un producto con el uuid: ${test.uuid}", find.message)
        coVerify(exactly=1){repository.findProductByUuid(test.uuid)}
    }

    @Test
    fun saveTest() = runTest{
        coEvery { repository.save(test) } returns test
        val save = service.saveProduct(test)
        assertAll(
            { assertEquals(test.name, save.name) },
            { assertEquals(test.price, save.price) },
            { assertEquals(test.available, save.available) },
            { assertEquals(test.description, save.description) },
            { assertEquals(test.url, save.url) },
            { assertEquals(test.category, save.category) },
            { assertEquals(test.stock, save.stock) },
            { assertEquals(test.brand, save.brand) },
            { assertEquals(test.model, save.model) }
        )
        coVerify(exactly = 1){repository.save(test)}
    }

    @Test
    fun updateTest() = runTest{
        coEvery { repository.save(test) } returns test
        val update = service.updateProduct(test, test)
        assertAll(
            { assertEquals(test.name, update.name) },
            { assertEquals(test.price, update.price) },
            { assertEquals(test.available, update.available) },
            { assertEquals(test.description, update.description) },
            { assertEquals(test.url, update.url) },
            { assertEquals(test.category, update.category) },
            { assertEquals(test.stock, update.stock) },
            { assertEquals(test.brand, update.brand) },
            { assertEquals(test.model, update.model) }
        )
        coVerify(exactly =1) { repository.save(test) }
    }

    @Test
    fun deleteTest() = runTest{
        coEvery { repository.findProductByUuid(test.uuid) } returns flowOf(test)
        coEvery { repository.delete(test) } returns Unit
        val delete = service.deleteProduct(test.uuid)
        assertTrue(delete)

        coVerify(exactly =1) { repository.findProductByUuid(test.uuid) }
        coVerify(exactly =1) { repository.delete(test) }
    }

    @Test
    fun deleteNotFoundTest() = runTest{
        coEvery { repository.findProductByUuid(test.uuid) } returns flowOf()
        val exception = org.junit.jupiter.api.assertThrows<ProductNotFoundException> {
            service.deleteProduct(test.uuid)
        }

        assertEquals("No existe el producto con uuid: ${test.uuid}", exception.message)

        coVerify(exactly =1) { repository.findProductByUuid(test.uuid) }
    }

}