package com.example.apiproducto.repositories

import com.example.apiproducto.models.Service
import com.example.apiproducto.models.ServiceCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@ExperimentalCoroutinesApi
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ServiceRepositoryTest {
    @Autowired
    lateinit var repository: ServiceRepository

    @Test
    fun findAllTest() = runTest {
        val res = repository.findAll().toList()

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.isNotEmpty()) }
        )
    }

    @Test
    fun findById() = runTest {
        val serviceTest = Service(
            price = 8.5,
            available = true,
            description = "Prueba",
            url = "url",
            category = ServiceCategory.CHANGE_OF_STRINGS,
        )
        val save = repository.save(serviceTest)
        val res = repository.findById(save.id!!)

        assertAll(
            { assertNotNull(res) },
            { assertEquals(serviceTest.url, res?.url) },
            { assertEquals(serviceTest.category, res?.category) },
            { assertEquals(serviceTest.price, res?.price) },
            { assertEquals(serviceTest.available, res?.available) }
        )

        repository.delete(serviceTest)
    }

    @Test
    fun findByUuid() = runTest {
        val serviceTest = Service(
            price = 8.5,
            available = true,
            description = "Prueba",
            url = "url",
            category = ServiceCategory.CHANGE_OF_STRINGS,
        )
        val save = repository.save(serviceTest)
        val res = repository.findServiceByUuid(save.uuid).firstOrNull()

        assertAll(
            { assertNotNull(res) },
            { assertEquals(serviceTest.url, res?.url) },
            { assertEquals(serviceTest.category, res?.category) },
            { assertEquals(serviceTest.price, res?.price) },
            { assertEquals(serviceTest.available, res?.available) }
        )

        repository.delete(serviceTest)
    }

    @Test
    fun findByIdNotFound() = runTest {
        val res = repository.findById(-1)
        assertNull(res)
    }

    @Test
    fun findByUuidNotFound() = runTest {
        val res = repository.findServiceByUuid("-1").firstOrNull()
        assertNull(res)
    }

    @Test
    fun saveTest() = runTest {
        val serviceTest = Service(
            price = 8.5,
            available = true,
            description = "Prueba",
            url = "url",
            category = ServiceCategory.CHANGE_OF_STRINGS,
        )
        val save = repository.save(serviceTest)
        assertAll(
            { assertNotNull(save) },
            { assertEquals(serviceTest.url, save.url) },
            { assertEquals(serviceTest.category, save.category) },
            { assertEquals(serviceTest.price, save.price) },
            { assertEquals(serviceTest.available, save.available) }
        )
        repository.delete(serviceTest)
    }

    @Test
    fun deleteTest() = runTest {
        val serviceTest = Service(
            price = 8.5,
            available = true,
            description = "Prueba",
            url = "url",
            category = ServiceCategory.CHANGE_OF_STRINGS,
        )
        val save = repository.save(serviceTest)
        repository.delete(save)
    }
}