package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.repositories.UsersRepository
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
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserRepositoryTest {
    @Autowired
    lateinit var repository: UsersRepository

    private val userTest: Users = Users(
        id = 1,
        uuid = "565432d8-3606-4aa6-b111-5951d0643b8d",
        email = "test@mail.com",
        name = "test",
        password = "1234",
        telephone = 123456789
    )


    @Test
    fun findByUuidTest() = runTest {
        val res = repository.findByUuid(userTest.uuid).toList()
        assertAll(
            { assertNotNull(res) }
        )
    }

    @Test
    fun findByNameTest() = runTest {
        val res = repository.findByName(userTest.name).toList()
        assertAll(
            { assertNotNull(res) }
        )
    }
}