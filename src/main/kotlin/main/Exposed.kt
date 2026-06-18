package main

import adapter.infrastructure.exposed.connectToDatabase
import io.ktor.server.application.*

fun Application.configureExposed() {
    val url = environment.config.property("postgres.url").getString()
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()

    if (url.isBlank() || user.isBlank() || password.isBlank()) {
        throw IllegalArgumentException("URL, user or password is blank in config")
    }

    if (! connectToDatabase(url, user, password)) {
        throw IllegalArgumentException("Database connection errors: $url");
    }
}
