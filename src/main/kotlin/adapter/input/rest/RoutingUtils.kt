package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.RoutingCall
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