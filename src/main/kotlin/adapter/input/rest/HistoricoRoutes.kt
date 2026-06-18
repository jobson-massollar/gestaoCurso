package adapter.input.rest

import adapter.input.ui.template.historicoAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import services.domain.persistence.AlunoRepository
import services.domain.persistence.RepositoryFactory

fun Routing.historicoRoutes() {
    get("/historico/{matricula}") {
        val matricula = call.parameters["matricula"] ?: ""

        val repo = RepositoryFactory.get(AlunoRepository::class)
        val aluno = repo.findByMatricula(matricula)

        if (aluno != null) {
            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                historicoAluno(aluno)
            }
        }
    }
}