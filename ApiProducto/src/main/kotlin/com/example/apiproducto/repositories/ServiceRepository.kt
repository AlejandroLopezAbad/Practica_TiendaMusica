package com.example.apiproducto.repositories

import com.example.apiproducto.models.Service
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : CoroutineCrudRepository<Service, Int>
