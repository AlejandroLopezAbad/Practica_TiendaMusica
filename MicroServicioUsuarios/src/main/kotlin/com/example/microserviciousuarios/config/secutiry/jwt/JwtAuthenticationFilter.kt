package com.example.microserviciousuarios.config.secutiry.jwt

import com.example.microserviciousuarios.dto.UsersLoginDto
import com.example.microserviciousuarios.models.Users
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

private val logger = KotlinLogging.logger {}


class JwtAuthenticationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val authenticationManagerX: AuthenticationManager,//TODO mirar que pasa qui
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(req: HttpServletRequest, response: HttpServletResponse): Authentication {
        logger.info { "Intentando autenticar" }

        val credentials = ObjectMapper().readValue(req.inputStream, UsersLoginDto::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.email,
            credentials.password,
        )
        return authenticationManagerX.authenticate(auth)
    }

    override fun successfulAuthentication(
        req: HttpServletRequest?, res: HttpServletResponse, chain: FilterChain?,
        auth: Authentication
    ) {
        logger.info { "Autenticación correcta" }

        // val username = (auth.principal as Usuario).username
        // val token: String = jwtTokenUtil.generateToken(username)
        val user = auth.principal as Users
        val token: String = jwtTokenUtil.generateToken(user)
        res.addHeader("Authorization", token)
        // Authorization
        res.addHeader("Access-Control-Expose-Headers", JwtTokenUtil.TOKEN_HEADER)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        logger.info { "Autenticación incorrecta" }

        val error = BadCredentialsError()
        response.status = error.status
        response.contentType = "application/json"
        response.writer.append(error.toString())
    }

}

private data class BadCredentialsError(
    val timestamp: Long = Date().time,
    val status: Int = 401,
    val message: String = "Users o password incorrectos",
) {
    override fun toString(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}