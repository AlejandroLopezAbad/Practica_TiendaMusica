package com.example.microserviciousuarios.services

import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.repositories.UsersRepository
import com.example.microserviciousuarios.services.users.UsersServices
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest


@ExtendWith(MockKExtension::class)
@SpringBootTest
class UsersServicesTest {

    private val userTest: Users = Users(
        id = 1,
        uuid = "565432d8-3606-4aa6-b111-5951d0643b8d",
        email = "test@mail.com",
        name = "test",
        password = "1234",
        telephone = 123456789
    )

    @MockK
    lateinit var repository: UsersRepository

    @InjectMockKs
    lateinit var service: UsersServices

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() = runTest {
        coEvery { repository.findAll() } returns flowOf(userTest)
        val res = service.findAll().toList()
        assertAll(
            { assertNotNull(res) },
            { assertEquals(1, res.size) },
            { assertTrue(res.contains(userTest)) }
        )

    }

    @Test
    fun loadUserById() = runTest {
        coEvery { repository.findById(userTest.id?.toLong()!!) } returns userTest
        val res = service.loadUserById(userTest.id?.toLong()!!)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(userTest.name, res?.name) },
            { assertEquals(userTest.id, res?.id) },
            { assertEquals(userTest.uuid, res?.uuid) },
            { assertEquals(userTest.email, res?.email) },
            { assertEquals(userTest.name, res?.name) },
            { assertEquals(userTest.password, res?.password) },
            { assertEquals(userTest.telephone, res?.telephone) }
        )
    }

    @Test
    fun loadUserbyUuid() = runTest {
        coEvery { repository.findByUuid(userTest.uuid) } returns flowOf(userTest)

        val res = service.loadUserbyUuid(userTest.uuid)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(userTest.name, res?.name) },
            { assertEquals(userTest.id, res?.id) },
            { assertEquals(userTest.uuid, res?.uuid) },
            { assertEquals(userTest.email, res?.email) },
            { assertEquals(userTest.name, res?.name) },
            { assertEquals(userTest.password, res?.password) },
            { assertEquals(userTest.telephone, res?.telephone) }
        )
    }

    @Test
    fun save() = runTest {
        coEvery { repository.save(userTest) } returns userTest

        val res = service.save(userTest)
        assertAll(
            { assertNotNull(res) },
            { assertEquals(userTest.name, res.name) },
            { assertEquals(userTest.id, res.id) },
            { assertEquals(userTest.uuid, res.uuid) },
            { assertEquals(userTest.email, res.email) },
            { assertEquals(userTest.name, res.name) },
            { assertEquals(userTest.password, res.password) },
            { assertEquals(userTest.telephone, res.telephone) }
        )
    }

    @Test
    fun update() = runTest {
        coEvery { repository.findByName(userTest.name) } returns flowOf(userTest)
        coEvery { repository.findByEmail(userTest.email) } returns flowOf(userTest)
        coEvery { repository.save(userTest) } returns userTest

        val res = service.update(userTest)

        assertAll(
            { assertNotNull(res) },
            { assertEquals(userTest.name, res.name) },
            { assertEquals(userTest.id, res.id) },
            { assertEquals(userTest.uuid, res.uuid) },
            { assertEquals(userTest.email, res.email) },
            { assertEquals(userTest.name, res.name) },
            { assertEquals(userTest.password, res.password) },
            { assertEquals(userTest.telephone, res.telephone) }
        )

    }
}