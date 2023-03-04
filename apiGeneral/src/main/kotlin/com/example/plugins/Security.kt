package com.example.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.TokenConfig
import com.example.service.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val tokenConfigParams = mapOf(
        "audience" to environment.config.property("jwt.audience").getString(),
        "secret" to environment.config.property("jwt.secret").getString(),
        "realm" to environment.config.property("jwt.realm").getString(),
        "expiration" to environment.config.property("jwt.expiration").getString()
    )

    val tokenConfig: TokenConfig = get { parametersOf(tokenConfigParams) }

    val jwtService: TokenService by inject()

    authentication {
        jwt {
            verifier(jwtService.verifyJWT())
            realm = tokenConfig.realm
            validate { credential ->
                // TODO hay que meterle que compruebe que el username no esta vacio
                if (credential.payload.audience.contains(tokenConfig.audience)
                )
                    JWTPrincipal(credential.payload)
                else null
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token invalido o expirado")
            }
        }
    }
}
