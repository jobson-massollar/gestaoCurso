package main

import adapter.input.rest.alunoRoutes
import adapter.input.rest.historicoRoutes
import adapter.input.rest.inscricaoRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        staticResources("/static", "static")

        alunoRoutes()
        historicoRoutes()
        inscricaoRoutes()
    }
}