package adapter.input.rest

import adapter.input.ui.historicoAluno
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.html.h1
import model.AlunoRepository
import model.RepositoryFactory

const val HISTORICO_ROUTE = "/historico"

fun Route.historicoRoutes() {

    get("$HISTORICO_ROUTE/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondBadRequest()

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null)
            call.respondHTML(status = HttpStatusCode.OK) {
                historicoAluno(aluno)
            }
        else
            call.respondHTML(HttpStatusCode.OK) {
                h1 { +"Aluno com matrícula $matricula não encontrado!"}
            }
    }
}