package com.example.model

data class User(
    val id: Int,
    var username: String,
    var firstName: String,
    var secondName: String,
    var password: String,
    var role: String,
    var group: String?
) {
    companion object {
        val exampleJohn = mapOf(
            "id" to 101,
            "username" to "jdoe",
            "firstName" to "John",
            "secondName" to "Doe",
            "password" to "5f4dcc3b5aa765d61d8327deb882cf99",
            "role" to "student",
            "group" to "EPM20"
        )
        val exampleJane = mapOf(
            "id" to 201,
            "username" to "jdoe1",
            "firstName" to "Jane",
            "secondName" to "Doe",
            "password" to "5f4dcc3b5aa765d61d8327deb882cf99",
            "role" to "student",
            "group" to "EPM19"
        )
    }
}
