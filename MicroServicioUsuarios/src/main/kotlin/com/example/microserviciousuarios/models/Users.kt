package com.example.microserviciousuarios.models


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

/**
 * Users model
 */
@Table(name = "users")
data class Users(
    @Id
    val id :Int?=null,
    @Column("uuid")
    val uuid:String= UUID.randomUUID().toString(),
    @Column("email")
    val email:String,
    @Column("name")
    val name:String,
    @get:JvmName("userPassword")
    @Column("password")
    val password:String,
    @Column("telephone")
    val telephone:Int = 787744741,
    @Column("rol")
    val rol :String=TypeRol.USER.name,
    @Column("avaliable")
    val avaliable:Boolean=true,
    @Column("url")
    val url:String="",
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val deleted: Boolean = false,
    ): UserDetails {


    enum class TypeRol() {
        USER,EMPLOYEE,ADMIN,SUPERADMIN
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        return rol.split(",").map { SimpleGrantedAuthority("ROLE_${it.trim()}") }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
     return name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }


}






