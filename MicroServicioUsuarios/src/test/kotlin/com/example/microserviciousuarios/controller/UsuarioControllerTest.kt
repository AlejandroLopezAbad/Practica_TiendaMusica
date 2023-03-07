package com.example.microserviciousuarios.controller

import com.example.microserviciousuarios.config.secutiry.jwt.JwtTokenUtil
import com.example.microserviciousuarios.dto.UsersCreateDto
import com.example.microserviciousuarios.dto.UsersLoginDto
import com.example.microserviciousuarios.dto.UsersUpdateDto
import com.example.microserviciousuarios.dto.UsersWithTokenDto
import com.example.microserviciousuarios.mappers.toDto
import com.example.microserviciousuarios.models.Users
import com.example.microserviciousuarios.services.storage.StorageService
import com.example.microserviciousuarios.services.users.UsersServices

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.client.RestTemplate

@ExtendWith(MockKExtension::class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UsuarioControllerTest {

    @MockK
    private lateinit var service: UsersServices

    @InjectMockKs
    lateinit var controller: UsuarioController

    private val userTest: Users = Users(
        id = 1,
        uuid = "565432d8-3606-4aa6-b111-5951d0643b8d",
        email = "test@mail.com",
        name = "test",
        password = "123456",
        telephone = 123456789
    )

    val usersCreateDto = UsersCreateDto(
        email = "test@mail.com",
        name = "test",
        password = "123456",
        telephone = "123456789"
    )

    val userUpdateDto = UsersUpdateDto(
        email = "updated@mail.com",
        name = "update",
        telephone = "123456789"
    )

    val userLoginDto = UsersLoginDto(
        "test@mail.com", "123456"
    )

    @MockK
    lateinit var usersServices: UsersServices

    @MockK
    lateinit var authenticationManager: AuthenticationManager


    @MockK
    lateinit var jwtTokenUtil: JwtTokenUtil

    @MockK
    lateinit var storageServices: StorageService

    @InjectMockKs
    lateinit var userController: UsuarioController


    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun loginSuccess() = runTest {

    }

    @Test
    @Order(1)
    fun register() = runTest{

    }

    @Test
    fun list() = runTest{

    }

    @Test
    fun meInfo() {
    }

    @Test
    fun updateMe() {
    }
}