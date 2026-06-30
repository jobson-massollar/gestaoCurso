package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import adapter.input.ui.tableAlunos
import adapter.input.ui.title
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import main.appLocale
import model.Aluno
import model.AlunoRepository
import model.RepositoryFactory

private data class Parameters(val sorting: String, val filter: String, val search: String)

fun Routing.alunoRoutes() {
    get("/") {
        call.respondHtmlTemplate(MainPageTemplate(), status = HttpStatusCode.OK) {
            content {}
        }
    }

    get("/alunos") {
        val params = getParameters(call)
        val alunos = findAlunos(params.sorting, params.filter, params.search)

        call.respondHtmlFragment(status = HttpStatusCode.OK) {
            title("Alunos", "/alunos/download?sort=${params.sorting}&filter=${params.filter}&search=${params.search}") {
                tableAlunos(alunos, params.sorting, params.filter, params.search)
            }
        }
    }

    get("/alunos/download") {
        val params = getParameters(call)
        val alunos = findAlunos(params.sorting, params.filter, params.search)

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"alunos.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Versao;Nome;Sexo;Data de nascimento;E-mail;Trancamento;Prazo de Extensão;Ingresso;Evasão;Data de evasão;Logradouro;Numero;Complemento;Bairro;Cidade;CEP;Telefone1;Telefone2\n" +
            alunos.joinToString(separator = "\n") { it.toCsv() }
        }
    }
}

private fun getParameters(call: RoutingCall) =
    Parameters(
        call.request.queryParameters["sort"] ?: ALUNO_SORTING_NOME_ASC,
        call.request.queryParameters["filter"] ?: ALUNO_FILTER_ACTIVE,
        (call.request.queryParameters["search"] ?: "").trim().lowercase(appLocale)
    )

private fun findAlunos(sorting: String, filter: String, search: String): List<Aluno> =
    RepositoryFactory.get(AlunoRepository::class)
        .findByFilter(getAlunoFilterByValue(filter), search)
        .sortedWith(getAlunoSortingByValue(sorting).comparator)


private fun Aluno.toCsv() = "${matricula};${versao};${nome};${sexo};${dataNascimento};${email};${trancamentos};${prazoExtensao};${ingresso};${evasao};${dataEvasao?:""};${logradouro};${numero};${complemento};${bairro};${cidade};${cep};${telefone1};${telefone2}"