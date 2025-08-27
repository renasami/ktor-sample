package com.example

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktoropenapi.route
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        route("api.json") {
            openApi()
        }

        route("swagger-ui") {
            swaggerUI("/api.json")
        }

        get("/", {
            description = "Welcome endpoint"
            response {
                HttpStatusCode.OK to {
                    description = "Welcome message"
                    body<MessageResponse>()
                }
            }
        }) {
            call.respond(MessageResponse("Welcome to the API"))
        }

        route("/api/v1", {
            tags = listOf("API v1")
        }) {
            get("/users", {
                description = "Get all users"
                response {
                    HttpStatusCode.OK to {
                        description = "List of users"
                        body<List<User>>()
                    }
                }
            }) {
                call.respond(listOf(
                    User(1, "John", "john@example.com"),
                    User(2, "Jane", "jane@example.com")
                ))
            }

            get("/users/{id}", {
                description = "Get user by ID"
                request {
                    pathParameter<Int>("id") {
                        description = "User ID"
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "User details"
                        body<User>()
                    }
                    HttpStatusCode.NotFound to {
                        description = "User not found"
                    }
                }
            }) {
                val id = call.parameters["id"]?.toIntOrNull()
                when(id) {
                    1 -> call.respond(User(1, "John", "john@example.com"))
                    2 -> call.respond(User(2, "Jane", "jane@example.com"))
                    else -> call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        }
    }
}

@Serializable
data class User(val id: Int, val name: String, val email: String)

@Serializable
data class MessageResponse(val message: String)