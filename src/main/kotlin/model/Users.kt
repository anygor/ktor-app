package com.example.model

data class Users(val users: MutableList<User>) {
    companion object {
        val exampleModel = mapOf(
            "users" to listOf(
                User.exampleJohn,
                User.exampleJane
            )
        )
    }
}