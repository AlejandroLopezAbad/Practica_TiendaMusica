package com.example.microserviciousuarios.services


import com.example.microserviciousuarios.models.User
import com.example.microserviciousuarios.repositories.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService
@Autowired constructor(
    private val repository: UserRepository
){
    suspend fun findUsersByName(name: String): List<User>{
        return repository.findUserByName(name).toList()
    }

    suspend fun findAllUsers():List<User>{
        return repository.findAll().toList()
    }

    suspend fun findUserById(id:Int):User?{
        return repository.findById(id)
    }

    suspend fun findProductByUuid(uuid:String): User?{
        return repository.findUserByUuid(uuid).firstOrNull()
    }

    suspend fun saveUser(user: User): User{
        return repository.save(user)
    }

    suspend fun deleteUser(user: User){
        repository.delete(user)
    }

    suspend fun updateUser(user: User, updateData:User): User{
        user.apply {
            email = updateData.email
            name = updateData.name
            password = updateData.password
            telephone = updateData.telephone
            role = updateData.role
            available = updateData.available
            url = updateData.url
        }
        return repository.save(user)
    }
}