package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.Users
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface UsersRepository: R2dbcRepository<Users, Long> {
    fun findUsersByUuid(uuid: UUID): Mono<Users>
}