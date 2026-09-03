package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.h1
import model.LogImportacaoRepository
import model.RepositoryFactory

suspend fun ApplicationCall.respondHTML(status: HttpStatusCode, fragment: FlowContent.() -> Unit) {
    respond(status, fragment)
}

suspend fun ApplicationCall.respondBadRequest() {
    respond(HttpStatusCode.OK) {
        h1 { +"Ooops! Algum parâmetro dessa chamada é inválido!" }
    }
}

suspend private fun ApplicationCall.respond(status: HttpStatusCode, fragment: FlowContent.() -> Unit) {
    if (request.headers["HX-Request"] == "true" && request.headers["HX-History-Restore-Request"] != "true") {
        respondHtmlFragment(status = status, fragment)
    }
    else {
        val version = application.environment.config.property("app.version").getString()
        val ultimaImportacao = RepositoryFactory.get(LogImportacaoRepository::class).findLast()?.dtProcessamento

        respondHtmlTemplate(MainPageTemplate(version, ultimaImportacao), status = status) {
            pageBody {
                mainContent {
                    fragment()
                }
            }
        }
    }
}