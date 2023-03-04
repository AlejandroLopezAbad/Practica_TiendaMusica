package com.example.apiproducto.services

import com.example.apiproducto.dto.ServiceUpdateDto
import com.example.apiproducto.exceptions.ServiceNotFoundException
import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory
import com.example.apiproducto.repositories.ServiceRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(MockKExtension::class)
@SpringBootTest
class ServicesServiceTest {
    @MockK
    private lateinit var repository: ServiceRepository

    @InjectMockKs
    private lateinit var service: ServicesService

    private val test = Service(
        id = 1,
        price = 2.5,
        available = true,
        description = "Descripción",
        url = "",
        category = ServiceCategory.AMPLIFIER_REPAIR,
    )
    private val testDelete = Service(
        id = 1,
        price = 2.5,
        available = false,
        description = "Descripción",
        url = "",
        category = ServiceCategory.AMPLIFIER_REPAIR,
    )
    private val updateTest = ServiceUpdateDto(
        price = 2.5,
        available = true,
        description = "Descripción",
        url = ""
    )

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAllTest() = runTest {
        coEvery { repository.findAll() } returns flowOf(test)
        val all = service.findAllServices()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(test.price, all[0].price) },
            { assertEquals(test.available, all[0].available) },
            { assertEquals(test.description, all[0].description) },
            { assertEquals(test.url, all[0].url) },
            { assertEquals(test.category, all[0].category) },

            )
        coVerify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun findByIdTest() = runTest {
        coEvery { repository.findById(test.id!!) } returns test
        val find = service.findServiceById(test.id!!)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.price, find.price) },
            { assertEquals(test.available, find.available) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.category, find.category) }
        )
        coVerify(exactly = 1) { repository.findById(test.id!!) }
    }

    @Test
    fun findProductByIdNotExistTest() = runTest {
        coEvery { repository.findById(test.id!!) } returns null
        val find = assertThrows<ServiceNotFoundException> {
            service.findServiceById(test.id!!)
        }
        assertTrue(find.message!!.contains("No se ha encontrado un servicio"))
        coVerify(exactly = 1) { repository.findById(test.id!!) }
    }

    @Test
    fun findByUuidTest() = runTest {
        coEvery { repository.findServiceByUuid(test.uuid) } returns flowOf(test)
        val find = service.findServiceByUuid(test.uuid)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.price, find.price) },
            { assertEquals(test.available, find.available) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.category, find.category) }
        )
        coVerify(exactly = 1) { repository.findServiceByUuid(test.uuid) }
    }

    @Test
    fun findProductByUuidNotExistTest() = runTest {
        coEvery { repository.findServiceByUuid(test.uuid) } returns flowOf()
        val find = assertThrows<ServiceNotFoundException> {
            service.findServiceByUuid(test.uuid)
        }
        assertTrue(find.message!!.contains("No se ha encontrado un servicio"))
        coVerify(exactly = 1) { repository.findServiceByUuid(test.uuid) }
    }

    @Test
    fun saveTest() = runTest {
        coEvery { repository.save(test) } returns test
        val save = service.saveService(test)
        assertAll(
            { assertEquals(test.price, save.price) },
            { assertEquals(test.available, save.available) },
            { assertEquals(test.description, save.description) },
            { assertEquals(test.url, save.url) },
            { assertEquals(test.category, save.category) }
        )
        coVerify(exactly = 1) { repository.save(test) }
    }

    @Test
    fun updateTest() = runTest {
        coEvery { repository.save(test) } returns test
        val update = service.updateService(test, updateTest)
        assertAll(
            { assertEquals(test.price, update.price) },
            { assertEquals(test.available, update.available) },
            { assertEquals(test.description, update.description) },
            { assertEquals(test.url, update.url) },
        )
        coVerify(exactly = 1) { repository.save(test) }
    }

    @Test
    fun deleteTest() = runTest {
        coEvery { repository.findServiceByUuid(test.uuid) } returns flowOf(test)
        coEvery { repository.deleteById(test.id!!) } returns Unit

        val delete = service.deleteService(test.uuid)

        assertTrue(delete)
        coVerify(exactly = 1) { repository.findServiceByUuid(test.uuid) }
        coVerify(exactly = 1) { repository.deleteById(test.id!!) }
    }

    @Test
    fun deleteNotExistTest() = runTest {
        coEvery { repository.findServiceByUuid(test.uuid) } returns emptyFlow()
        val find = assertThrows<ServiceNotFoundException> {
            service.deleteService(test.uuid)
        }
        assertTrue(find.message!!.contains("No existe el servicio"))

        coVerify(exactly = 1) { repository.findServiceByUuid(test.uuid) }
    }

}