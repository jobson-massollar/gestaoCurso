package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.mainRoutes() {
    get("/") {
        val version = call.application.environment.config.property("app.version").getString()

        call.respondHtmlTemplate(MainPageTemplate(version), status = HttpStatusCode.OK) {
        }
    }
}