package com.example.apiproducto.models

abstract class Item(
    open var id: Int?,
    open var uuid: String,
    open var price: Double,
    open var available: Boolean,
    open var description: String,
    open var url: String,
)
