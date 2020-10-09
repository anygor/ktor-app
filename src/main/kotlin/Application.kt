package com.example

import com.example.repository.User
import com.example.repository.Users
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.ArrayList

private val logger = Logger()

fun Routing.rout(){
    get("/users") {
        var json: String = ""
        transaction {
            val res = Users.selectAll().orderBy(Users.id)
            val list = ArrayList<User>()
            for (user in res)
                list.add(User(
                    id = user[Users.id],
                    username = user[Users.username],
                    firstName = user[Users.firstName],
                    secondName = user[Users.secondName],
                    password = user[Users.password],
                    role = user[Users.role],
                    group = user[Users.group]
                ))
            json = Gson().toJson(list)
        }
        call.respondText(json, ContentType.Text.Html)
    }
}

fun initDB() {
    val url = "jdbc:mysql://nativeuser:password@localhost:3306/journal?useUnicode=true&serverTimezone=UTC"
    val driver = "com.mysql.jdbc.Driver"
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

