package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import adapter.input.ui.tableAlunos
import adapter.input.ui.painelAluno
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import main.appLocale
import model.AlunoRepository
import model.RepositoryFactory

fun Routing.alunoRoutes() {
    get("/") {
        call.respondHtmlTemplate(MainPageTemplate(), status = HttpStatusCode.OK) {
            content {}
        }
    }

    get("/alunos") {
        val sorting = call.request.queryParameters["sort"] ?: ALUNO_SORTING_NOME_ASC
        val filter = call.request.queryParameters["filter"] ?: ALUNO_FILTER_ACTIVE
        val search = (call.request.queryParameters["search"] ?: "").trim().lowercase(appLocale)

        val comparator = getAlunoSortingByValue(sorting).comparator

        val repo = RepositoryFactory.get(AlunoRepository::class)
        val alunos = repo.findByFilter(getAlunoFilterByValue(filter), search).sortedWith(comparator)

        call.respondHtmlFragment(status = HttpStatusCode.OK) {
            tableAlunos(alunos, sorting, filter, search)
        }
    }

    get("/alunos/painel/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) {}

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null) {
            call.respondHtmlFragment(status = HttpStatusCode.OK) {
                painelAluno(aluno)
            }
        }
    }
}