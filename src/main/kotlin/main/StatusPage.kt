package main

import adapter.input.rest.respondHTML
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.html.h1

fun Application.configureStatusPage() {
    install(StatusPages) {

        status(HttpStatusCode.NotFound) { call, status ->
            call.respondHTML(status) {
                h1 { +"Ooops! Página não encontrada!" }
            }
        }

        status(HttpStatusCode.InternalServerError) { call, status ->
            call.respondHTML(status) {
                h1 { +"Ooops! Houve um problema para responder sua requisição." }
                h1 { +"Por favor, volte e tente novamente..." }
            }
        }
    }
}