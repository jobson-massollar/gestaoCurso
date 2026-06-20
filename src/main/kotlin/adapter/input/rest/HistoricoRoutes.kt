package adapter.input.rest

import adapter.input.ui.historicoAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import services.domain.persistence.AlunoRepository
import services.domain.persistence.DisciplinaRepository
import services.domain.persistence.ItemHistoricoRepository
import services.domain.persistence.RepositoryFactory

fun Routing.historicoRoutes() {
    get("/historico/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val repoAluno = RepositoryFactory.get(AlunoRepository::class)
        val aluno = repoAluno.findByMatricula(matricula)

        if (aluno != null) {
            val repoHistorico = RepositoryFactory.get(ItemHistoricoRepository::class)
            val repoDisciplina = RepositoryFactory.get(DisciplinaRepository::class)

            val historico = repoHistorico.findByMatricula(matricula).sortedBy { it.ano*10+it.periodo }
            val aprovadas = repoHistorico.findAprovados(matricula).sortedBy { it.ano*10+it.periodo }
            val matriculadas = repoHistorico.findMatriculados(matricula).sortedBy { it.ano*10+it.periodo }
            val obrigatoriasFaltantes = repoDisciplina.findObrigatoriasFaltantes(matricula).sortedBy { it.codigo }

            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                historicoAluno(aluno, historico, aprovadas, matriculadas, obrigatoriasFaltantes)
            }
        }
    }
}