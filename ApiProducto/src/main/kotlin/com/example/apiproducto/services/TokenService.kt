package com.example.apiproducto.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.apiproducto.exceptions.InvalidTokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Servicio de token para la seguridad.
 */
@Service
class TokenService {
    @Value("\${jwt.secret}")
    private var secret: String? = null


    /**
     * Verificar el token.
     * @param token token a verifricar si es correcto.
     * @throws InvalidTokenException si el token es inválido.
     * @return el token verificado.
     */
    fun tokenVerify(token: String): DecodedJWT? {
        try {
            return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
        } catch (e: Exception) {
            throw InvalidTokenException("Token inválido o expirado")
        }
    }


    /**
     * Conseguir los claims de un token.
     * @param token token a saber sus claims.
     * @return los claims del token pedido.
     */
    fun getClaims(token: String) = tokenVerify(token)?.claims!!


    /**
     * Conseguir los roles de un token.
     * @param token token a saber sus roles.
     * @return los roles del token.
     */
    fun getRoles(token: String): String = getClaims(token)["roles"]!!.asString()

}