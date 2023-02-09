package com.example.microserviciousuarios.models

import com.example.microserviciousuarios.models.enums.TypeRol
import java.util.*

data class Users(
    val id :Long,
    val uuid:UUID= UUID.randomUUID(),
    val email:String,
    val name:String,
    val password:ByteArray,
    val telephone:Int,
    val rol : TypeRol,
    val avaliable:Boolean,
    val url:String,
    ) {
}


