package com.example.apiproducto.controller

import com.example.apiproducto.dto.ProductDto
import com.example.apiproducto.dto.ProductResponseDto
import com.example.apiproducto.exceptions.ProductNotFoundException
import com.example.apiproducto.mappers.toProduct
import com.example.apiproducto.models.Product
import com.example.apiproducto.models.ProductCategory
import com.example.apiproducto.services.ProductService
import com.example.apiproducto.services.TokenService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ProductControllerTest {

    @MockK
    private lateinit var tokenService: TokenService

    @MockK
    private lateinit var service: ProductService

    @InjectMockKs
    private lateinit var controller: ProductController

    private val test = Product(
        id = 1, uuid = UUID.randomUUID().toString(), name = "Test", price = 2.50, available = true,
        description = "Prueba descripcion", url = "url", category = ProductCategory.BOOSTER, stock = 10,
        brand = "marca", model = "model"
    )

    private val testDto = ProductDto(
        name = "Test", price = 2.50, available = true,
        description = "Prueba descripcion", url = "url", category = "BOOSTER", stock = 10,
        brand = "marca", model = "model"
    )


    init {
        MockKAnnotations.init(this)
    }


    @Test
    fun getAllProducts() = runTest {
        coEvery { service.findAllProducts() } returns listOf(test)
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        val all = controller.getAllProducts("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw")
        val result = all.body as List<ProductResponseDto>
        assertAll(
            { assertNotNull(result) },
            { assertTrue(result?.isNotEmpty()!!) },
            { assertEquals(test.uuid, result!![0].uuid) },
            { assertEquals(test.name, result!![0].name) },
            { assertEquals(test.price, result!![0].price) },
            { assertEquals(test.available, result!![0].available) },
            { assertEquals(test.description, result!![0].description) },
            { assertEquals(test.url, result!![0].url) },
            { assertEquals(test.category.name, result!![0].category) },
            { assertEquals(test.stock, result!![0].stock) },
            { assertEquals(test.brand, result!![0].brand) },
            { assertEquals(test.model, result!![0].model) }
        )

        coVerify(exactly = 1) { service.findAllProducts() }
        coVerify(exactly =1) {tokenService.getRoles(any())}
    }


    @Test
    fun findProductByUuid() = runTest {
        coEvery { service.findProductByUuid(test.uuid) } returns test
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        val find = controller.findProductByUuid("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
            test.uuid)
        val result = find.body as ProductResponseDto

        assertAll(
            { assertNotNull(result) },
            { assertEquals(test.uuid, result.uuid) },
            { assertEquals(test.name, result.name) },
            { assertEquals(test.price, result.price) },
            { assertEquals(test.available, result.available) },
            { assertEquals(test.description, result.description) },
            { assertEquals(test.url, result.url) },
            { assertEquals(test.category.name, result.category) },
            { assertEquals(test.stock, result.stock) },
            { assertEquals(test.brand, result.brand) },
            { assertEquals(test.model, result.model) }
        )

        coVerify(exactly = 1) { service.findProductByUuid(test.uuid) }
        coVerify(exactly =1) {tokenService.getRoles(any())}
    }


    @Test
    fun findProductByUuidNotFound() = runTest {
        coEvery { service.findProductByUuid(test.uuid) } throws ProductNotFoundException("No se ha encontrado un producto con el uuid: ${test.uuid}")
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val find = assertThrows<ResponseStatusException> {
            controller.findProductByUuid("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                test.uuid)
        }
        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el uuid: ${test.uuid}"""",
            find.message
        )
        coVerify(exactly = 1) { service.findProductByUuid(test.uuid) }
        coVerify(exactly =1) {tokenService.getRoles(any())}
    }

    @Test
    fun findProductByUuidNotAvailable() = runTest {
        coEvery { service.findProductByUuid(test.uuid) } throws ProductNotFoundException("No se ha encontrado un producto con el uuid: ${test.uuid}")
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val find = assertThrows<ResponseStatusException> {
            controller.findProductByUuid("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                test.uuid)
        }
        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el uuid: ${test.uuid}"""",
            find.message
        )

        coVerify(exactly = 1) { service.findProductByUuid(test.uuid) }
        coVerify(exactly =1) {tokenService.getRoles(any())}
    }


//    @Test
//    fun saveProduct() = runTest{
//        coEvery { service.saveProduct(test) } returns test
//        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
//
//        val save = controller.saveProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
//            testDto)
//        val result = save.body
//
//        assertAll(
//            { assertNotNull(result) },
//            { assertEquals(test.uuid, result!!.uuid)},
//            { assertEquals(test.name, result!!.name) },
//            { assertEquals(test.price, result!!.price) },
//            { assertEquals(test.available, result!!.available) },
//            { assertEquals(test.description, result!!.description) },
//            { assertEquals(test.url, result!!.url) },
//            { assertEquals(test.category.name, result!!.category) },
//            { assertEquals(test.stock, result!!.stock) },
//            { assertEquals(test.brand, result!!.brand) },
//            { assertEquals(test.model, result!!.model) }
//        )
//
//        coVerify(exactly=1){service.saveProduct(test)}
//        coVerify(exactly =1) {tokenService.getRoles(any())}
//    }


    @Test
    fun updateProduct() = runTest {
        coEvery { service.findProductByUuid(test.uuid) } returns test
        coEvery { service.updateProduct(any(), any()) } returns test
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val update = controller.updateProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
            testDto, test.uuid)
        val result = update.body

        assertAll(
            { assertNotNull(result) },
            { assertEquals(test.uuid, result!!.uuid) },
            { assertEquals(test.name, result!!.name) },
            { assertEquals(test.price, result!!.price) },
            { assertEquals(test.available, result!!.available) },
            { assertEquals(test.description, result!!.description) },
            { assertEquals(test.url, result!!.url) },
            { assertEquals(test.category.name, result!!.category) },
            { assertEquals(test.stock, result!!.stock) },
            { assertEquals(test.brand, result!!.brand) },
            { assertEquals(test.model, result!!.model) }
        )

        coVerify(exactly = 1) { service.findProductByUuid(test.uuid) }
        coVerify(exactly = 1) { service.updateProduct(any(), any()) }
        coVerify(exactly = 1) { tokenService.getRoles(any()) }
    }


    @Test
    fun updateProductNotFound() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.findProductByUuid(test.uuid) } throws ProductNotFoundException("No se ha encontrado un producto con el uuid: ${test.uuid}")
        val update = assertThrows<ResponseStatusException> {
            controller.updateProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                testDto, test.uuid)
        }
        assertEquals(
            """404 NOT_FOUND "No se ha encontrado un producto con el uuid: ${test.uuid}"""",
            update.message
        )

        coVerify(exactly = 1) { tokenService.getRoles(any()) }
        coVerify(exactly = 1) { service.findProductByUuid(test.uuid) }
    }


    @Test
    fun updateProductUnauthorized() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "USER"
        val update = assertThrows<ResponseStatusException> {
            controller.updateProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                testDto, test.uuid)
        }
        assertEquals(
            """401 UNAUTHORIZED "No tienes permisos para realizar esta acción"""",
            update.message
        )
        coVerify(exactly = 1) { tokenService.getRoles(any()) }
    }


    @Test
    fun deleteProduct() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "SUPERADMIN"
        coEvery { service.deleteProduct(test.uuid) } returns true

        val delete = controller.deleteProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
            test.uuid)
        val result = delete.statusCode
        assertTrue(result == HttpStatus.NO_CONTENT)

        coVerify(exactly = 1) { service.deleteProduct(test.uuid) }
        coVerify(exactly = 1) { tokenService.getRoles(any()) }
    }


    @Test
    fun deleteProductNotFound() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "SUPERADMIN"
        coEvery { service.deleteProduct(test.uuid) } throws ProductNotFoundException("No existe el producto con uuid: ${test.uuid}")
        val delete = assertThrows<ResponseStatusException> {
            controller.deleteProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                test.uuid)
        }

        assertEquals(
            """404 NOT_FOUND "No existe el producto con uuid: ${test.uuid}"""",
            delete.message
        )

        coVerify(exactly = 1) { service.deleteProduct(test.uuid) }
        coVerify(exactly = 1) { tokenService.getRoles(any()) }
    }


    @Test
    fun deleteProductUnauthorized() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "USER"
        val update = assertThrows<ResponseStatusException> {
            controller.deleteProduct("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIn0.NWXV_I8wlkuymOCEl3typzIS7HKEawi-m4gQolPBodpJV9CmD738hz9Z3HyUAcNWNPdPcfekfcIcLhzHAbFwTw",
                test.uuid)
        }
        assertEquals(
            """401 UNAUTHORIZED "No tienes permisos para realizar esta acción"""",
            update.message
        )
        coVerify(exactly = 1) { tokenService.getRoles(any()) }
    }
}