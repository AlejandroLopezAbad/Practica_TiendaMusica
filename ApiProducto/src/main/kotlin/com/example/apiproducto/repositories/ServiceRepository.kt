package com.example.apiproducto.repositories

import com.example.apiproducto.models.Service
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ServiceRepository : CoroutineCrudRepository<Service, Long>