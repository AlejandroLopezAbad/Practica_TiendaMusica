package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.Users
import kotlinx.coroutines.flow.Flow

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository : CoroutineCrudRepository<Users, Long> {

    fun findByUuid(uuid: String): Flow<Users>
    fun findByName(name: String): Flow<Users>
    fun findByEmail(email:String):Flow<Users>


}