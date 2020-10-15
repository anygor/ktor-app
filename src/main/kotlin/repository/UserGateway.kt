package com.example.repository

import com.example.model.User
import org.jetbrains.exposed.sql.*

interface UserGateway {
    fun getUsers(): List<User?>
    fun createUser(newUser: User): Boolean
    fun updateUser(user: User)
    fun getUser(id: Int): User?
    fun deleteUser(id: Int)
}

object Users : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val username = varchar("username", 64)
    val firstName = varchar("firstName", 64)
    val secondName = varchar("secondName", 64)
    val password = varchar("password", 64)
    val role = varchar("role", 45)
    val group = varchar("group", 45).nullable()
}

private fun toUser(row: ResultRow): User {
    return User(
        id = row[Users.id],
        username = row[Users.username],
        firstName = row[Users.firstName],
        secondName = row[Users.secondName],
        password = row[Users.password],
        role = row[Users.role],
        group = row[Users.group]
    )
}

class UserRepository : UserGateway {
    override fun getUsers(): List<User?> {
        return Users.selectAll().map { toUser(it) }
    }

    override fun createUser(newUser: User): Boolean {
        val id = Users.insert {
            it[username] = newUser.username
            it[firstName] = newUser.firstName
            it[secondName] = newUser.secondName
            it[password] = newUser.password
            it[role] = newUser.role
            it[group] = newUser.group
        } get Users.id

        return id ?: -1 >= 0
    }

    override fun updateUser(user: User) {
        Users.update({ Users.id eq user.id }) {
            it[username] = user.username
            it[firstName] = user.firstName
            it[secondName] = user.secondName
            it[password] = user.password
            it[role] = user.role
            it[group] = user.group
        }
    }

    override fun getUser(id: Int): User? {
        val query = Users.select { Users.id.eq(id) }.firstOrNull() ?: return null
        return User(
            query[Users.id],
            query[Users.username],
            query[Users.firstName],
            query[Users.secondName],
            query[Users.password],
            query[Users.role],
            query[Users.group]
        )
    }

    override fun deleteUser(id: Int) {
        Users.deleteWhere {
            Users.id eq id
        }
    }
}


