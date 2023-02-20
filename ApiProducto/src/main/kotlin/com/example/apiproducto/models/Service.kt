package com.example.apiproducto.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "services")
data class Service(
    @Id
    override var id: Int? = null,
    override var uuid: String = UUID.randomUUID().toString(),
    override var price: Double,
    override var available: Boolean,
    override var description: String,
    override var url: String,
    var category: ServiceCategory,
) : Item(id, uuid, price, available, description, url)

enum class ServiceCategory {
    GUITAR_REPAIR, AMPLIFIER_REPAIR, CHANGE_OF_STRINGS
}
