package com.example.apiproducto.models

import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "services")
data class Service(
    override var id: Long? = null,
    override var uuid: UUID = UUID.randomUUID(),
    override var name: String,
    override var price: Double,
    override var available: Boolean,
    override var description: String,
    override var url: String,
    var category: ServiceCategory,
) : Item(id, uuid, name, price, available, description, url) {
    enum class ServiceCategory {
        GUITAR_REPAIR, AMPLIFIER_REPAIR, CHANGE_OF_STRINGS
    }
}
