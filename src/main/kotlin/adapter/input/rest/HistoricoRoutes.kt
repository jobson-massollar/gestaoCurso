package adapter.input.rest

import adapter.input.ui.historicoAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import services.domain.persistence.AlunoRepository
import services.domain.persistence.ItemHistoricoRepository
import services.domain.persistence.RepositoryFactory

fun Routing.historicoRoutes() {
    get("/historico/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val repoAluno = RepositoryFactory.get(AlunoRepository::class)
        val repoHistorico = RepositoryFactory.get(ItemHistoricoRepository::class)
        val aluno = repoAluno.findByMatricula(matricula)
        val historico = repoHistorico.findByMatricula(matricula).sortedBy { it.ano*10+it.periodo }
        val histAprovados = repoHistorico.findAprovados(matricula).sortedBy { it.ano*10+it.periodo }

        if (aluno != null) {
            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                historicoAluno(aluno, historico, histAprovados)
            }
        }
    }
}