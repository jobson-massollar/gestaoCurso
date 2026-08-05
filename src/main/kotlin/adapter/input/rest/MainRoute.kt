package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.mainRoutes() {
    get("/") {
        call.respondHtmlTemplate(MainPageTemplate(), status = HttpStatusCode.OK) {
        }
    }
}