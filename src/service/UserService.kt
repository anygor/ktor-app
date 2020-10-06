package com.example.service

import com.example.repository.User
import com.example.repository.UserGateway

interface UserService {
    fun create(newUser: User): Boolean
    fun delete(id: Int)
    fun getAll(): List<User?>
    fun get(id: Int): User?
    fun update(user: User)
}

class UserServiceImp(private val userGateway: UserGateway) : UserService {
    override fun create(newUser: User): Boolean {
        return this.userGateway.createUser(newUser)
    }

    override fun delete(id: Int) {
        return this.userGateway.deleteUser(id)
    }

    override fun getAll(): List<User?> {
        return this.userGateway.getUsers()
    }

    override fun get(id: Int): User? {
        return this.userGateway.getUser(id)
    }

    override fun update(user: User) {
        return this.userGateway.updateUser(user)
    }
}