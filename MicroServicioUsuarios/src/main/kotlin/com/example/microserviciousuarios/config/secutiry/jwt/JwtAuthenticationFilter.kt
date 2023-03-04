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

/**
 * Clase que realiza la autenticación del usuario con Json Web Token
 */
class JwtAuthenticationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val authenticationManagerX: AuthenticationManager,
) : UsernamePasswordAuthenticationFilter() {

    /**
     * Attempt authentication
     * @param req
     * @param response
     * @return
     */
    override fun attemptAuthentication(req: HttpServletRequest, response: HttpServletResponse): Authentication {
        logger.info { "Intentando autenticar" }

        val credentials = ObjectMapper().readValue(req.inputStream, UsersLoginDto::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.email,
            credentials.password,
        )
        return authenticationManagerX.authenticate(auth)
    }

    /**
     * Successful authentication
     *
     * @param req
     * @param res
     * @param chain
     * @param auth
     */
    override fun successfulAuthentication(
        req: HttpServletRequest?, res: HttpServletResponse, chain: FilterChain?,
        auth: Authentication
    ) {
        logger.info { "Autenticación correcta" }


        val user = auth.principal as Users
        val token: String = jwtTokenUtil.generateToken(user)
        res.addHeader("Authorization", token)
        res.addHeader("Access-Control-Expose-Headers", JwtTokenUtil.TOKEN_HEADER)
    }

    /**
     * Unsuccessful authentication
     *
     * @param request
     * @param response
     * @param failed
     */
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

/**
 * Clase que maneja los errores de las credenciales.
 */
private data class BadCredentialsError(
    val timestamp: Long = Date().time,
    val status: Int = 401,
    val message: String = "Users o password incorrectos",
) {
    override fun toString(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}