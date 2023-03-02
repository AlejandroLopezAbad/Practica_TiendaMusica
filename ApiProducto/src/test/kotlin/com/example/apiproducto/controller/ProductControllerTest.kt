package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.services.ProductService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ProductControllerTest {
    @MockK
    private lateinit var service: ProductService
    @InjectMockKs
    private lateinit var controller: ProductController

    private val test = Product(id=1, uuid= UUID.randomUUID().toString(), name="Test", price = 2.50, available=true,
        description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
        brand = "marca", model = "model")
    private val testDto = ProductDto(name="Test", price = 2.50, available=true,
        description = "Prueba descripcion", url = "url", category = "BOOSTER", stock = 10,
        brand = "marca", model = "model")


    init {
        MockKAnnotations.init(this)
    }


    @Test
    fun getAllProducts() = runTest{
        coEvery { service.findAllProducts() } returns listOf(test)
        val all = controller.getAllProducts()
        val result = all.body
        assertAll(
            { assertNotNull(result) },
            { assertTrue(result?.isNotEmpty()!!) },
            { assertEquals(test.id, result!![0].id) },
            { assertEquals(test.uuid, result!![0].uuid)},
            { assertEquals(test.name, result!![0].name) },
            { assertEquals(test.price, result!![0].price) },
            { assertEquals(test.available, result!![0].available) },
            { assertEquals(test.description, result!![0].description) },
            { assertEquals(test.url, result!![0].url) },
            { assertEquals(test.category, result!![0].category) },
            { assertEquals(test.stock, result!![0].stock) },
            { assertEquals(test.brand, result!![0].brand) },
            { assertEquals(test.model, result!![0].model) }
        )

        coVerify(exactly = 1){service.findAllProducts()}
    }


    @Test
    fun findProductById() = runTest{
        coEvery { service.findProductById(test.id!!) } returns test
        val find = controller.findProductById(test.id!!)
        val result = find.body

        assertAll(
            { assertNotNull(result) },
            { assertEquals(test.id, result!!.id) },
            { assertEquals(test.uuid, result!!.uuid)},
            { assertEquals(test.name, result!!.name) },
            { assertEquals(test.price, result!!.price) },
            { assertEquals(test.available, result!!.available) },
            { assertEquals(test.description, result!!.description) },
            { assertEquals(test.url, result!!.url) },
            { assertEquals(test.category, result!!.category) },
            { assertEquals(test.stock, result!!.stock) },
            { assertEquals(test.brand, result!!.brand) },
            { assertEquals(test.model, result!!.model) }
        )

        coVerify(exactly=1){service.findProductById(test.id!!)}
    }


    @Test
    fun findProductByIdNotFound() = runTest {
        coEvery { service.findProductById(test.id!!) } throws ProductNotFoundException("No se ha encontrado un producto con el id: ${test.id}")
        val find = assertThrows<ResponseStatusException>{
            controller.findProductById(test.id!!)
        }
        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el id: ${test.id}"""",
            find.message
        )
        coVerify(exactly=1){service.findProductById(test.id!!)}
    }


//    TODO No funciona
//    @Test
//    fun saveProduct() = runTest{
//        coEvery { service.saveProduct(any()) } returns test
//        val save = controller.saveProduct(testDto)
//        val result = save.body
//
//        assertAll(
//            { assertNotNull(result) },
//            { assertEquals(test.id, result!!.id) },
//            { assertEquals(test.uuid, result!!.uuid)},
//            { assertEquals(test.name, result!!.name) },
//            { assertEquals(test.price, result!!.price) },
//            { assertEquals(test.available, result!!.available) },
//            { assertEquals(test.description, result!!.description) },
//            { assertEquals(test.url, result!!.url) },
//            { assertEquals(test.category, result!!.category) },
//            { assertEquals(test.stock, result!!.stock) },
//            { assertEquals(test.brand, result!!.brand) },
//            { assertEquals(test.model, result!!.model) }
//        )
//        coVerify(exactly=1){service.saveProduct(test)}
//    }


    @Test
    fun updateProduct() = runTest{
        coEvery { service.findProductById(test.id!!) } returns test
        coEvery { service.updateProduct(any(), any()) } returns test
        val update = controller.updateProduct(testDto, test.id!!)
        val result = update.body
        assertAll(
            { assertNotNull(result) },
            { assertEquals(test.id, result!!.id) },
            { assertEquals(test.uuid, result!!.uuid)},
            { assertEquals(test.name, result!!.name) },
            { assertEquals(test.price, result!!.price) },
            { assertEquals(test.available, result!!.available) },
            { assertEquals(test.description, result!!.description) },
            { assertEquals(test.url, result!!.url) },
            { assertEquals(test.category, result!!.category) },
            { assertEquals(test.stock, result!!.stock) },
            { assertEquals(test.brand, result!!.brand) },
            { assertEquals(test.model, result!!.model) }
        )

        coVerify(exactly=1){service.findProductById(test.id!!)}
        coVerify(exactly=1){service.updateProduct(any(), any())}
    }


    @Test
    fun updateProductNotFound() = runTest{
        coEvery { service.findProductById(test.id!!) } throws ProductNotFoundException("No se ha encontrado un producto con el id: ${test.id}")
        val update = assertThrows<ResponseStatusException>{
            controller.updateProduct(testDto, test.id!!)
        }
        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el id: ${test.id}"""",
            update.message
        )
        coVerify(exactly = 1){service.findProductById(test.id!!)}
    }


    @Test
    fun deleteProduct() = runTest{
        coEvery { service.findProductById(test.id!!) } returns test
        coEvery { service.deleteProduct(test) } returns Unit
        val delete = controller.deleteProduct(test.id!!)
        val result = delete.statusCode
        assertTrue(result == HttpStatus.NO_CONTENT)

        coVerify(exactly = 1){ service.findProductById(test.id!!) }
        coVerify(exactly = 1){ service.deleteProduct(test) }
    }

    @Test
    fun deleteProductNotFound() = runTest{
        coEvery { service.findProductById(test.id!!) } throws ProductNotFoundException("No se ha encontrado un producto con el id: ${test.id}")
        val delete = assertThrows<ResponseStatusException>{
            controller.deleteProduct(test.id!!)
        }

        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el id: ${test.id}"""",
            delete.message
        )
        coVerify(exactly = 1){ service.findProductById(test.id!!) }
    }
}