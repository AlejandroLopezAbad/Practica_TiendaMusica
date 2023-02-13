package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.Users
import kotlinx.coroutines.flow.Flow

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository : CoroutineCrudRepository<Users, Long> {
    fun findByUuid(uuid: UUID): Flow<Users>
    fun findByName(username: String): Flow<Users>


}