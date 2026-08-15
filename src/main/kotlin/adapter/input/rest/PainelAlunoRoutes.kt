package adapter.input.rest

import adapter.input.ui.painelAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.h1
import model.AlunoRepository
import model.RepositoryFactory

const val PAINEL_ALUNO_ROUTE = "/painel"

fun Routing.painelAlunoRoutes() {
    get("$PAINEL_ALUNO_ROUTE/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondBadRequest()

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null) {
            call.respondHTML(status = HttpStatusCode.OK) {
                painelAluno(aluno)
            }
        }
        else
            call.respondHTML(HttpStatusCode.OK) {
                h1 { +"Aluno com matrícula $matricula não encontrado!"}
            }
    }
}