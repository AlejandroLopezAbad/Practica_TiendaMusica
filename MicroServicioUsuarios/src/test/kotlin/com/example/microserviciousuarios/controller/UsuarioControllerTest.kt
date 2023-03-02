package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.users.UsersServices

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

//TODO(Estoy con esto)
@ExtendWith(MockKExtension::class)
@SpringBootTest
class UsuarioControllerTest {

    @MockK
    private lateinit var service : UsersServices

    @InjectMockKs
    lateinit var controller: UsuarioController

    private val userTest: Users = Users(
        id = 1,
        uuid = "565432d8-3606-4aa6-b111-5951d0643b8d",
        email = "test@mail.com",
        name = "test",
        password = "1234",
        telephone = 123456789
    )
    init {
        MockKAnnotations.init(this)
    }
    @Test
    fun login() {
    }

    @Test
    fun register() {
    }

    @Test
    fun list() {
    }

    @Test
    fun meInfo() {
    }

    @Test
    fun updateMe() {
    }
}