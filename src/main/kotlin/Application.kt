package com.example

import org.jetbrains.exposed.sql.*

private val logger = Logger()

fun initDB() {
    Database.connect(
        "jdbc:mysql://localhost:3306/journal?useUnicode=true&serverTimezone=UTC",
        driver = "com.mysql.jdbc.Driver",
        user = "nativeuser", password = "password"
    )
    logger.log("Connected successfully")
}

fun main(args: Array<String>) {
    initDB()
    SwagLauncher().main(args)
}

