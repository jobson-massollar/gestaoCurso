package adapter.input.rest

import adapter.input.ui.painelAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import model.AlunoRepository
import model.RepositoryFactory

fun Routing.painelAlunoRoutes() {
    get("/alunos/painel/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null) {
            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                painelAluno(aluno)
            }
        }
    }
}