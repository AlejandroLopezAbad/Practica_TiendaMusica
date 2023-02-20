package com.example.apiproducto.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "products")
data class Product(
    @Id
    override var id: Int? = null,
    override var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    override var price: Double,
    override var available: Boolean,
    override var description: String,
    override var url: String,
    var category: ProductCategory,
    var stock: Int,
    var brand: String,
<<<<<<< HEAD
    var model: String
): Item(id, uuid, price, available, description, url)
=======
    var model: String,
) : Item(id, uuid, price, available, description, url)
>>>>>>> c6a6bd8ac6d160b0fae5964b6615c56980e80c02

enum class ProductCategory {
    GUITAR, BASS_GUITAR, BOOSTER, ACCESSORY
}
