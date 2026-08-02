package adapter.input.rest

import adapter.input.ui.tableDiario
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import main.collator
import model.DisciplinaRepository
import model.RepositoryFactory

fun Route.diarioRoutes() {

    get("/diario/{versao}/{codigo}") {
        val versao = call.parameters["versao"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}
        val codigo = call.parameters["versao"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val disciplina = RepositoryFactory.get(DisciplinaRepository::class).findByCode(versao, codigo)
        val itensDiario = disciplina.itensDiario

        call.respondHTML(HttpStatusCode.OK) {
            tableDiario(
                disciplina,
                itensDiario.sortedWith(compareBy(collator) {
                    it.nome
                })
            )
        }
    }
}