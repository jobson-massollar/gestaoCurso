package adapter.input.rest

import adapter.input.ui.historicoAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import model.AlunoRepository
import model.RepositoryFactory

const val HISTORICO_ROUTE = "/historico"

fun Route.historicoRoutes() {

    get("$HISTORICO_ROUTE/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) { }

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null)
            call.respondHTML(status = HttpStatusCode.OK) {
                historicoAluno(aluno)
            }
        else
            call.respondHtml(HttpStatusCode.NotFound) {}
    }
}