package com.example.apiproducto.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.apiproducto.exceptions.InvalidTokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TokenService {
    @Value("\${jwt.secret}")
    private var secret: String? = null

    fun tokenVerify(token: String): DecodedJWT? {
        try {
            return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
        } catch (e: Exception) {
            throw InvalidTokenException("Token inválido o expirado")
        }
    }

    fun getClaims(token: String) = tokenVerify(token)?.claims!!

    fun getRoles(token: String): String = getClaims(token)["roles"]!!.asString()

}