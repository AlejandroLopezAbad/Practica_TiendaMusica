package com.example.apiproducto.models

import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "products")
data class Product(
    override var id: Long,
    override var uuid: UUID,
    override var name: String,
    override var price: Double,
    override var available: Boolean,
    override var description: String,
    override var url: String,
    var category: String,
    var stock: Int,
    var brand: String,
    var model: String
): Item(id, uuid, name, price, available, description, url)
