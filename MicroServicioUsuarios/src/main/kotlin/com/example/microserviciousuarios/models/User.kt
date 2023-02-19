package com.example.microserviciousuarios.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "users")
data class User(
    @Id
    var id:Int? = null,
    var uuid: String = UUID.randomUUID().toString(),
    var email: String,
    var name: String,
    var password: String,
    var telephone: String,
    var role: RoleCategory,
    var available: Boolean,
    var url: String
)
enum class RoleCategory {
    SUPERADMIN, ADMIN, EMPLOYE, USER
}
