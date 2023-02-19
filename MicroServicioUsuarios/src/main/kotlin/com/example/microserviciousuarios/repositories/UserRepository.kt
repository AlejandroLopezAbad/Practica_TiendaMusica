package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: CoroutineCrudRepository<User, Int> {
    fun findUserByName(name: String): Flow<User>
    fun findUserByUuid(uuid: String): Flow<User>
}