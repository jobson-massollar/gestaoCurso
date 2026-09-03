package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import model.LogImportacaoRepository
import model.RepositoryFactory

fun Routing.mainRoutes() {
    get("/") {
        val version = call.application.environment.config.property("app.version").getString()
        val ultimaImportacao = RepositoryFactory.get(LogImportacaoRepository::class).findLast()?.dtProcessamento

        call.respondHtmlTemplate(MainPageTemplate(version, ultimaImportacao), status = HttpStatusCode.OK) {
        }
    }
}