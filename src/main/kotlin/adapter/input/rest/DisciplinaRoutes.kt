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
        val sorting = call.request.queryParameters["sort"] ?: DISCIPLINA_SORTING_NOME_ASC

        val disciplinas = RepositoryFactory.get(DisciplinaRepository::class).findAll()
            .filter { it.isObrigatoria && it.versao == versao }
            .sortedWith(getDisciplinaSortingByValue(sorting).comparator)

        call.respondHTML(HttpStatusCode.OK) {
            tableObrigatorias(disciplinas, versao, sorting)
        }
    }
}