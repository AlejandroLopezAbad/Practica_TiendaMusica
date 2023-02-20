package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory
import com.example.apiproducto.services.ServicesService
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
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ServiceControllerTest {
    @MockK
    private lateinit var service: ServicesService

    @InjectMockKs
    lateinit var controller: ServiceController

    private val serviceTest = Service(
        price = 2.5,
        available = true,
        description = "Descripción",
        url = "",
        category = ServiceCategory.AMPLIFIER_REPAIR,
    )

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() = runTest {
        coEvery { service.findAllServices() } returns flowOf(serviceTest)

        val result = controller.findAll()
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res, listOf(serviceTest)) }
        )
        coVerify { service.findAllServices() }
    }

    // TODO faltarian los test de validacion que aun no esta implementada
    @Test
    fun create() = runTest {
        coEvery { service.saveService(any()) } returns serviceTest

        val result = controller.create(
            ServiceCreateDto(
                serviceTest.price,
                serviceTest.available,
                serviceTest.description,
                serviceTest.url,
                serviceTest.category.name
            )
        )
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.CREATED) },
            { assertEquals(res.price, serviceTest.price) },
            { assertEquals(serviceTest.category, res.category) },
            { assertEquals(serviceTest.url, res.url) },
            { assertEquals(serviceTest.available, res.available) }
        )
        coVerify { service.saveService(any()) }
    }

    @Test
    fun findById() = runTest {
        coEvery { service.findById(any()) } returns serviceTest

        val result = controller.findById(1)
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res.price, serviceTest.price) },
            { assertEquals(serviceTest.category, res.category) },
            { assertEquals(serviceTest.url, res.url) },
            { assertEquals(serviceTest.available, res.available) }
        )
        coVerify { service.findById(any()) }
    }

    @Test
    fun findByIdNotFound() = runTest {
        coEvery { service.findById(any()) } returns null

        val result = controller.findById(-1)

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.NOT_FOUND) },
        )
        coVerify { service.findById(any()) }
    }

    @Test
    fun delete() = runTest {
        coEvery { service.deleteService(any()) } returns true

        val result = controller.delete(1)

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.NO_CONTENT) },
        )
        coVerify { service.deleteService(any()) }
    }

    @Test
    fun deleteNotFound() = runTest {
        coEvery { service.deleteService(any()) } throws ServiceNotFoundException("No existe el servicio con id")

        val result = assertThrows<ResponseStatusException> {
            controller.delete(-1)
        }

        assertAll(
            { assertTrue(result.message.contains("No existe el servicio")) },
        )
        coVerify { service.deleteService(any()) }
    }

    @Test
    fun update() = runTest {
        coEvery { service.findById(any()) } returns serviceTest
        coEvery { service.updateService(any(), any()) } returns serviceTest

        val result = controller.update(1, serviceTest)
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res.price, serviceTest.price) },
            { assertEquals(serviceTest.category, res.category) },
            { assertEquals(serviceTest.url, res.url) },
            { assertEquals(serviceTest.available, res.available) }
        )
        coVerify { service.findById(any()) }
        coVerify { service.updateService(any(), any()) }
    }

    @Test
    fun updateNotFound() = runTest {
        coEvery { service.findById(any()) } returns null

        val result = controller.update(1, serviceTest)

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.NOT_FOUND) },
        )
        coVerify { service.findById(any()) }
    }
}