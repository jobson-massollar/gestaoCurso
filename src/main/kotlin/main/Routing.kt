package main

import adapter.input.rest.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        staticResources("/static", "static")

        mainRoutes()
        alunoRoutes()
        painelAlunoRoutes()
        inscricoesRoutes()
        historicoRoutes()
        situacaoIrregularRoutes()
        turmaRoutes()
        diarioRoutes()
    }
}