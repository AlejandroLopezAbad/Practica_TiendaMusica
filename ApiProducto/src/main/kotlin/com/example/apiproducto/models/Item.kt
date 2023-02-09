package com.example.apiproducto.models

import java.util.UUID

data class Item(
    var id: Long,
    var uuid: UUID,
    var name: String,
    var price: Double,
    var available: Boolean,
    var description: String,
    var url: String
){
}