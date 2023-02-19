package com.example.microserviciousuarios.dto


data class UserDto (
    var email: String,
    var name: String,
    var password: String,
    var telephone: String,
    var role: String,
    var available: Boolean,
    var url: String
)