package adapter.input.rest

import adapter.input.ui.tableAlunos
import adapter.input.ui.tableInscricoes
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import main.appLocale
import model.AlunoRepository
import model.RepositoryFactory
import services.application.AlunoFilter

fun Route.inscricaoRoutes() {

    get("/inscricoes/irregulares") {
//        val sorting = call.request.queryParameters["sort"] ?: ALUNO_SORTING_NOME_ASC
//        val filter = call.request.queryParameters["filter"] ?: ALUNO_FILTER_ACTIVE
//        val search = (call.request.queryParameters["search"] ?: "").trim().lowercase(appLocale)
//        val comparator = getAlunoSortingByValue(sorting).comparator

        val repo = RepositoryFactory.get(AlunoRepository::class)
        val alunos = repo.findByFilter(AlunoFilter.ACTIVE).filter { it.estaIrregular }.sortedBy { it.itensMatriculados.size }

        call.respondHtmlFragment(status = HttpStatusCode.OK) {
            tableInscricoes(alunos)
        }
    }
}