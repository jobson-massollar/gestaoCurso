package main

import adapter.input.rest.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        staticResources("/static", "static")

        alunoRoutes()
        painelAlunoRoutes()
        inscricoesRoutes()
        historicoRoutes()
        jubilamentoRoutes()
        turmaRoutes()
        diarioRoutes()
    }
}