package adapter.input.rest

import adapter.input.ui.tableObrigatorias
import io.ktor.http.*
import io.ktor.server.routing.*
import model.DisciplinaRepository
import model.RepositoryFactory

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