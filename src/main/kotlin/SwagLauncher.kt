package com.example

import com.example.model.User
import com.example.model.Users
import com.example.repository.UserRepository
import com.example.service.UserService
import com.example.service.UserServiceImp
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import de.nielsfalk.ktor.swagger.SwaggerSupport
import de.nielsfalk.ktor.swagger.created
import de.nielsfalk.ktor.swagger.description
import de.nielsfalk.ktor.swagger.example
import de.nielsfalk.ktor.swagger.examples
import de.nielsfalk.ktor.swagger.get
import de.nielsfalk.ktor.swagger.ok
import de.nielsfalk.ktor.swagger.post
import de.nielsfalk.ktor.swagger.responds
import de.nielsfalk.ktor.swagger.version.shared.Contact
import de.nielsfalk.ktor.swagger.version.shared.Group
import de.nielsfalk.ktor.swagger.version.shared.Information
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.transactions.transaction

private val userService = UserServiceImp(UserRepository()) as UserService

@Group("user operations")
@Location("/users")
class users

var data = transaction { userService.getAll() }

class SwagLauncher : CliktCommand(
    name = "ktor-app"
) {
    companion object {
        private const val defaultPort = 8080
    }

    private val port: Int by option(
        "-p",
        "--port",
        help = "The port that this server should be started on. Defaults to $defaultPort."
    )
        .int()
        .default(defaultPort)

    override fun run() {
        run(port)
    }
}

internal fun run(port: Int, wait: Boolean = true): ApplicationEngine {
    Logger().log("Launching on port `$port`")
    val server = embeddedServer(Netty, port) {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Locations)
        install(SwaggerSupport) {
            forwardRoot = true
            val information = Information(
                version = "0.5",
                title = "API for Ktor Journal App",
                description = "App for journaling marks of students based on [ktor](https://github.com/Kotlin/ktor) with [swaggerUi](https://swagger.io/). You find the sources on [github](https://github.com/anygor/ktor-app)"
            )
            swagger = Swagger().apply {
                info = information
            }
            openApi = OpenApi().apply {
                info = information
            }
        }
        routing {
            get<users>("all".responds(ok<Users>(example("model", Users.exampleModel)))) {
                data = transaction { userService.getAll() }
                call.respond(data)
            }
            post<users, User>(
                "create"
                    .description("Save a user in our wonderful database!")
                    .examples(
                        example("john", User.exampleJohn, summary = "John is one possible user."),
                        example("jane", User.exampleJane, summary = "Jane is a different posssible user.")
                    )
                    .responds(
                        created<User>(
                            example("john", User.exampleJohn),
                            example("jane", User.exampleJane)
                        )
                    )
            ) { _, entity ->
                call.respond(Created, entity.copy(id = 1).apply {
                    transaction {userService.create(entity)}
                })
            }
        }
    }
    return server.start(wait = wait)
}