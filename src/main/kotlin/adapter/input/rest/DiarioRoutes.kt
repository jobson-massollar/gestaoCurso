package adapter.input.rest

import adapter.input.ui.tableDiario
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import main.collator
import model.DisciplinaRepository
import model.RepositoryFactory

const val DIARIO_ROUTE = "/diario"

fun Route.diarioRoutes() {

    get("$DIARIO_ROUTE/{turma}/{versao}/{codigo}") {
        val turma = call.parameters["turma"]?.replace('-', '/') ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}
        val versao = call.parameters["versao"]?.replace('-', '/') ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}
        val codigo = call.parameters["codigo"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val disciplina = RepositoryFactory.get(DisciplinaRepository::class).findByCode(versao, codigo)
        val itensDiario = disciplina.itensDiario(turma)

        call.respondHTML(HttpStatusCode.OK) {
            tableDiario(
                turma,
                disciplina,
                itensDiario.sortedWith(compareBy(collator) {
                    it.nome
                })
            )
        }
    }
}