package com.example.microserviciousuarios.repositories

import com.example.microserviciousuarios.models.Users
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


/**
 * Users repository
 *
 * @constructor Create empty Users repository
 */

@Repository
interface UsersRepository : CoroutineCrudRepository<Users, Long> {

    /**
     * Find by uuid
     *
     * @param uuid
     * @return
     */
    fun findByUuid(uuid: String): Flow<Users>

    /**
     * Find by name
     *
     * @param name
     * @return
     */
    fun findByName(name: String): Flow<Users>

    /**
     * Find by email
     *
     * @param email
     * @return
     */
    fun findByEmail(email:String):Flow<Users>

    /**
     * Find by telephone
     *
     * @param telephone
     * @return
     */
    fun findByTelephone(telephone: Int): Flow<Users>
}