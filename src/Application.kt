package com.example

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.ArrayList

private val logger = Logger()

fun main(args: Array<String>) {
    initDB()
    embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                logger.log(getTopUsers())
            }
        }
    }.start(wait = true)
}

fun initDB() {
    val url = "jdbc:mysql://nativeuser:password@localhost:3306/shop?useUnicode=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    Database.connect(url, driver)
}

object Users : Table("users"){
    val username = varchar("name", length = 45)
    val id = integer("id")
}

fun getTopUsers(): String{
    var json: String = ""
    transaction {
        val res = Users.selectAll().orderBy(Users.id)
        println(res)
        val list = ArrayList<User>()
        for (u in res){
            list.add(User(id = u[Users.id], name = u[Users.username]))
        }
        json = Gson().toJson(list)
    }
    return json
}
