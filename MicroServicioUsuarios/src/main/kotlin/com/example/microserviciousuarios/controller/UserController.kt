package com.example.microserviciousuarios.controller


import com.example.microserviciousuarios.dto.UserDto
import com.example.microserviciousuarios.mappers.toUser
import com.example.microserviciousuarios.models.User
import com.example.microserviciousuarios.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController
@Autowired constructor(
    private var service: UserService
){
    @GetMapping("")
    suspend fun getAllUsers(): ResponseEntity<List<User>>{
        val all = service.findAllUsers()
        return ResponseEntity.ok(all)
    }

    @GetMapping("/{id}")
    suspend fun findUsertById(@PathVariable id: Int): ResponseEntity<User>{
        val find = service.findUserById(id)
        find?.let {
            return ResponseEntity.ok(it)
        }?:run{
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("")
    suspend fun saveUser(@RequestBody dto: UserDto):ResponseEntity<User>{
        val user = dto.toUser()
        val created = service.saveUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    suspend fun updateUser(
        @RequestBody dto: UserDto,
        @PathVariable id: Int
    ): ResponseEntity<User>{
        val find = service.findUserById(id)
        find?.let {
            val dtoUser = dto.toUser()
            val updated = service.updateUser(it, dtoUser)
            return ResponseEntity.ok(updated)
        }?: run{
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteUser(@PathVariable id:Int): ResponseEntity<User> {
        val find = service.findUserById(id)
        find?.let {
            service.deleteUser(it)
            return ResponseEntity.noContent().build()
        }?: run{
            return ResponseEntity.notFound().build()
        }
    }

}