package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.FlowContent

suspend fun RoutingCall.respondHTML(status: HttpStatusCode, fragment: FlowContent.() -> Unit) {
    if (request.headers["HX-Request"] == "true" && request.headers["HX-History-Restore-Request"] != "true") {
        respondHtmlFragment(status = status, fragment)
    }
    else {
        respondHtmlTemplate(MainPageTemplate(), status = status) {
            pageBody {
                mainContent {
                    fragment()
                }
            }
        }
    }
}