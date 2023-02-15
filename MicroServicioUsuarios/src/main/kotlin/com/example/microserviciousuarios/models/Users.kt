package com.example.microserviciousuarios.models


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("users")
data class Users(
    @Id
    val id :Long,
    @Column("uuid")
    val uuid:UUID= UUID.randomUUID(),
    @Column("email")
    val email:String,
    @Column("name")
    val name:String,
    @Column("password")
    val password:ByteArray,
    @Column("telephone")
    val telephone:Int,
    @Column("rol")
    val rol : String= TypeRol.USER.name,
    @Column("avaliable")
    val avaliable:Boolean,
    @Column("url")
    val url:String,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false, // Para el borrado lógico si es necesario
    )/*:UserDetails*/ {

    enum class TypeRol() {
        USER,EMPLOYE,ADMIN,SUPERADMIN

    }
}
//TODO añadir userDetails cuando metamos la parte de seguridad
/*
// transformamos el conjunto de roles en una lista de GrantedAuthority
override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    //val ga = SimpleGrantedAuthority("ROLE_" + rol.name)
    // return mutableListOf<GrantedAuthority>(ga)
    return rol.split(",").map { SimpleGrantedAuthority("ROLE_${it.trim()}") }.toMutableList()}*/



