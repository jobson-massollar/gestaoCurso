package adapter.input.rest

import adapter.input.ui.tableObrigatorias
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import main.collator
import model.DisciplinaRepository
import model.RepositoryFactory
import kotlin.text.replace

const val DISCIPLINA_ROUTE = "/disciplina"

fun Route.disciplinaRoutes() {

    get("$DISCIPLINA_ROUTE/{versao}") {
        val versao = call.parameters["versao"]?.replace('-', '/') ?: return@get call.respondBadRequest()

        val disciplinas = RepositoryFactory.get(DisciplinaRepository::class).findAll()
            .filter { it.isObrigatoria && it.versao == versao }
            .sortedWith { d1, d2 -> collator.compare(d1.nome, d2.nome)  }

        call.respondHTML(HttpStatusCode.OK) {
            tableObrigatorias(versao, disciplinas)
        }
    }
}