package adapter.input.rest

import adapter.input.ui.historicoAluno
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import model.AlunoRepository
import model.RepositoryFactory

fun Route.historicoRoutes() {

    get("/historico/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) { }

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null)
            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                historicoAluno(aluno)
            }
        else
            call.respondHtml(HttpStatusCode.NotFound) {}
    }
}