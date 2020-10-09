package com.example

import com.example.repository.User
import com.example.repository.UserRepository
import com.example.service.UserService
import com.example.service.UserServiceImp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

private val logger = Logger()

private val userService = UserServiceImp(UserRepository()) as UserService

fun Routing.rout() {
    get("/users") {
        val users = transaction {
            userService.getAll()
        }
        call.respond(users.toString())
    }
    post("/users") {
        logger.log("Initiated post request")
        val userString = call.receive<String>()
        val userJson = JsonParser().parse(userString).asJsonObject
        val password = "5f4dcc3b5aa765d61d8327deb882cf99"

        var username = userJson.get("username").toString()
        username = username.substring(1, username.length - 1)
        var firstName = userJson.get("firstName").toString()
        firstName = firstName.substring(1, firstName.length - 1)
        var secondName = userJson.get("secondName").toString()
        secondName = secondName.substring(1, secondName.length - 1)
        var role = userJson.get("role").toString()
        role = role.substring(1, role.length - 1)
        var group = userJson.get("group").toString()
        group = group.substring(1, group.length - 1)

        val user = User(
            id = 300,
            username = username,
            firstName = firstName,
            secondName = secondName,
            password = password,
            role = role,
            group = group
        )
        transaction {
            userService.create(user)
        }
        call.respond("Affirmative user addition")
    }
}

fun initDB() {
    Database.connect(
        "jdbc:mysql://localhost:3306/journal?useUnicode=true&serverTimezone=UTC",
        driver = "com.mysql.jdbc.Driver",
        user = "nativeuser", password = "password"
    )
}


fun main(args: Array<String>) {
    initDB()
    embeddedServer(Netty, 8080) {
        routing {
            rout()
        }
    }.start(wait = true)
}

