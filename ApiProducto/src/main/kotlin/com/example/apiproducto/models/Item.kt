package com.example.apiproducto.models

import org.springframework.data.annotation.Id
import java.util.*

abstract class Item(
    @Id
    open var id: Long?,
    open var uuid: UUID,
    open var name: String,
    open var price: Double,
    open var available: Boolean,
    open var description: String,
    open var url: String,
)