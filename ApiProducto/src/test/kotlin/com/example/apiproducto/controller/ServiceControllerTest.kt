package com.example.apiproducto.controller

import com.example.apiproducto.dto.ServiceCreateDto
import com.example.apiproducto.dto.ServiceDto
import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.InvalidTokenException
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.mappers.toServiceDto
import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory
import com.example.apiproducto.services.ServicesService
import com.example.apiproducto.services.TokenService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ServiceControllerTest {
    @MockK
    private lateinit var tokenService: TokenService

    @MockK
    private lateinit var service: ServicesService

    @InjectMockKs
    lateinit var controller: ServiceController

    private val serviceTest = Service(
        price = 2.5,
        available = true,
        description = "Descripción",
        url = "url",
        category = ServiceCategory.AMPLIFIER_REPAIR,
    )
    private val serviceTestNoAvailable = Service(
        price = 2.5,
        available = false,
        description = "Descripción",
        url = "",
        category = ServiceCategory.AMPLIFIER_REPAIR,
    )
    private val serviceUpdate = ServiceUpdateDto(
        description = "Descripción",
        price = 2.5,
        url = "url",
        available = false,
    )

    private val tokenSuperadmin =
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlNVUEVSQURNSU4iLCJhdWQiOiJqd3QtYXVkaWVuY2UifQ.BKDdDfiexwjtoF-nYxu8Bv1NdLU-zAp7Ugm1z_PVe4Mh2XKev23mPjRJxpiUsSucb5VTKvQh6zFGeKz0YiAADg"
    private val tokenAdmin =
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IkFETUlOIiwiYXVkIjoiand0LWF1ZGllbmNlIn0.QFnmJ0ZT5jdbn8L4Y-OlHJmkYBL97qLjaNCit_A8W2B2T4zZxZWCMoEaAyWD4FNZOlyx4HSRhaI5JozE2r_y4Q"

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() = runTest {
        coEvery { service.findAllServices() } returns listOf(serviceTest)
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val result = controller.getAllServices(tokenSuperadmin)
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res, listOf(serviceTest)) }
        )
        coVerify { service.findAllServices() }
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findAllByUser() = runTest {
        coEvery { service.findAllServices() } returns listOf(serviceTest)

        val result = controller.getAllServices(null)
        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res, listOf(serviceTest.toServiceDto())) }
        )
        coVerify { service.findAllServices() }
    }

    @Test
    fun findAllNoAuthorization() = runTest {
        coEvery { tokenService.getRoles(any()) } returns ""

        val thrown = assertThrows<ResponseStatusException> {
            controller.getAllServices(tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findAllInvalidToken() = runTest {
        coEvery { tokenService.getRoles(any()) } throws InvalidTokenException("Token inválido o expirado")

        val thrown = assertThrows<ResponseStatusException> {
            controller.getAllServices(tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)
        assertTrue(thrown.message.contains("Token inválido"))

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findByUuid() = runTest {
        coEvery { service.findServiceByUuid(any()) } returns serviceTest
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val result = controller.findByUuid(serviceTest.uuid, tokenSuperadmin)
        val res = result.body!!
        val rest = res as Service

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(rest.price, serviceTest.price) },
            { assertEquals(serviceTest.category, rest.category) },
            { assertEquals(serviceTest.url, rest.url) },
            { assertEquals(serviceTest.available, rest.available) }
        )
        coVerify { service.findServiceByUuid(any()) }
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findByUuidByUser() = runTest {
        coEvery { service.findServiceByUuid(any()) } returns serviceTest

        val result = controller.findByUuid(serviceTest.uuid, null)
        val res = result.body!!
        val rest = res as ServiceDto

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(rest.price, serviceTest.price) },
            { assertEquals(serviceTest.category, rest.category) },
            { assertEquals(serviceTest.url, rest.url) },
        )
        coVerify { service.findServiceByUuid(any()) }
    }

    @Test
    fun findByUuidByUserNoAvailable() = runTest {
        coEvery { service.findServiceByUuid(any()) } returns serviceTestNoAvailable

        val thrown = assertThrows<ResponseStatusException> {
            controller.findByUuid(serviceTest.uuid, null)
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.NOT_FOUND) },
        )
        coVerify { service.findServiceByUuid(any()) }
    }

    @Test
    fun findByUuidNoAuthorization() = runTest {
        coEvery { tokenService.getRoles(any()) } returns ""

        val thrown = assertThrows<ResponseStatusException> {
            controller.findByUuid(serviceTest.uuid, tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findByUuidInvalidToken() = runTest {
        coEvery { tokenService.getRoles(any()) } throws InvalidTokenException("Token inválido o expirado")

        val thrown = assertThrows<ResponseStatusException> {
            controller.findByUuid(serviceTest.uuid, tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)
        assertTrue(thrown.message.contains("Token inválido"))

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun findByUuidNotFound() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.findServiceByUuid(any()) } throws ServiceNotFoundException("No existe")

        val thrown = assertThrows<ResponseStatusException> {
            controller.findByUuid(serviceTest.uuid, tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.NOT_FOUND)
        assertTrue(thrown.message.contains("No existe"))

        coVerify { tokenService.getRoles(any()) }
        coVerify { service.findServiceByUuid(any()) }
    }

    @Test
    fun create() = runTest {
        coEvery { service.saveService(any()) } returns serviceTest
        coEvery { tokenService.getRoles(any()) } returns "SUPERADMIN"

        val result = controller.saveService(
            tokenSuperadmin,
            ServiceCreateDto(
                serviceTest.category.name,
                serviceTest.description,
                serviceTest.price,
                serviceTest.url,
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
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun createForbidden() = runTest {
        coEvery { tokenService.getRoles(any()) } returns ""

        val thrown = assertThrows<ResponseStatusException> {
            controller.saveService(
                tokenSuperadmin,
                ServiceCreateDto(
                    serviceTest.category.name,
                    serviceTest.description,
                    serviceTest.price,
                    serviceTest.url,
                )
            )
        }

        assertEquals(thrown.statusCode, HttpStatus.FORBIDDEN)

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun createBadRequest() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "SUPERADMIN"

        val thrown = assertThrows<ResponseStatusException> {
            controller.saveService(
                tokenSuperadmin,
                ServiceCreateDto(
                    serviceTest.category.name,
                    serviceTest.description,
                    serviceTest.price,
                    ""
                )
            )
        }

        assertEquals(thrown.statusCode, HttpStatus.BAD_REQUEST)
        assertTrue(thrown.message.contains("La url no puede estar vacía"))

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun createInvalidToken() = runTest {
        coEvery { tokenService.getRoles(any()) } throws InvalidTokenException("Token inválido o expirado")

        val thrown = assertThrows<ResponseStatusException> {
            controller.saveService(
                tokenSuperadmin,
                ServiceCreateDto(
                    serviceTest.category.name,
                    serviceTest.description,
                    serviceTest.price,
                    serviceTest.url,
                )
            )
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)
        assertTrue(thrown.message.contains("Token inválido"))

        coVerify { tokenService.getRoles(any()) }
    }


    @Test
    fun delete() = runTest {
        coEvery { service.deleteService(any()) } returns true
        coEvery { tokenService.getRoles(any()) } returns "SUPERADMIN"

        val result = controller.delete(tokenSuperadmin, serviceTest.uuid)

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.NO_CONTENT) },
        )
        coVerify { service.deleteService(any()) }
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun deleteByAdmin() = runTest {
        coEvery { service.notAvailableService(any()) } returns true
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"

        val result = controller.delete(tokenSuperadmin, serviceTest.uuid)

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.NO_CONTENT) },
        )
        coVerify { service.notAvailableService(any()) }
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun deleteForbidden() = runTest {
        coEvery { tokenService.getRoles(any()) } returns ""

        val thrown = assertThrows<ResponseStatusException> {
            controller.delete(tokenSuperadmin, serviceTest.uuid)
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.FORBIDDEN) },
            { assertTrue(thrown.message.contains("No tienes permiso")) }
        )
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun deleteNotFound() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.notAvailableService(serviceTest.uuid) } throws ServiceNotFoundException("No existe el servicio")

        val thrown = assertThrows<ResponseStatusException> {
            controller.delete(tokenSuperadmin, serviceTest.uuid)
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.NOT_FOUND) },
            { assertTrue(thrown.message.contains("No existe el servicio")) }
        )
        coVerify { tokenService.getRoles(any()) }
        coVerify { service.notAvailableService(any()) }
    }

    @Test
    fun deleteInvalidToken() = runTest {
        coEvery { tokenService.getRoles(any()) } throws InvalidTokenException("Token inválido o expirado")

        val thrown = assertThrows<ResponseStatusException> {
            controller.delete(serviceTest.uuid, tokenSuperadmin)
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)
        assertTrue(thrown.message.contains("Token inválido"))

        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun update() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.findServiceByUuid(any()) } returns serviceTest
        coEvery { service.updateService(serviceTest, serviceUpdate) } returns serviceTest

        val result = controller.update(
            tokenSuperadmin, serviceTest.uuid, serviceUpdate
        )

        val res = result.body!!

        assertAll(
            { assertEquals(result.statusCode, HttpStatus.OK) },
            { assertEquals(res.price, serviceTest.price) },
            { assertEquals(serviceTest.category, res.category) },
            { assertEquals(serviceTest.url, res.url) },
            { assertEquals(serviceTest.available, res.available) }
        )

        coVerify { tokenService.getRoles(any()) }
        coVerify { service.findServiceByUuid(any()) }
        coVerify { service.updateService(serviceTest, serviceUpdate) }
    }

    @Test
    fun updateForbidden() = runTest {
        coEvery { tokenService.getRoles(any()) } returns ""

        val thrown = assertThrows<ResponseStatusException> {
            controller.update(
                tokenSuperadmin, serviceTest.uuid, serviceUpdate
            )
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.FORBIDDEN) },
            { assertTrue(thrown.message.contains("No tienes permiso")) }
        )
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun updateNotFound() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.findServiceByUuid(any()) } throws ServiceNotFoundException("No se ha encontrado un servicio")

        val thrown = assertThrows<ResponseStatusException> {
            controller.update(
                tokenSuperadmin, serviceTest.uuid, serviceUpdate
            )
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.NOT_FOUND) },
            { assertTrue(thrown.message.contains("No se ha encontrado un servicio")) }
        )
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun updateBadRequest() = runTest {
        coEvery { tokenService.getRoles(any()) } returns "ADMIN"
        coEvery { service.findServiceByUuid(any()) } returns serviceTest

        val thrown = assertThrows<ResponseStatusException> {
            controller.update(
                tokenSuperadmin, serviceTest.uuid, ServiceUpdateDto(
                    "",
                    -1.0,
                    "",
                    false
                )
            )
        }

        assertAll(
            { assertEquals(thrown.statusCode, HttpStatus.BAD_REQUEST) },
            { assertTrue(thrown.message.contains("La descripción no puede estar vacía")) }
        )
        coVerify { tokenService.getRoles(any()) }
    }

    @Test
    fun updateInvalidToken() = runTest {
        coEvery { tokenService.getRoles(any()) } throws InvalidTokenException("Token inválido o expirado")

        val thrown = assertThrows<ResponseStatusException> {
            controller.update(
                tokenSuperadmin, serviceTest.uuid, ServiceUpdateDto(
                    serviceTest.description,
                    serviceTest.price,
                    serviceTest.url,
                    true
                )
            )
        }

        assertEquals(thrown.statusCode, HttpStatus.UNAUTHORIZED)
        assertTrue(thrown.message.contains("Token inválido"))

        coVerify { tokenService.getRoles(any()) }
    }

}