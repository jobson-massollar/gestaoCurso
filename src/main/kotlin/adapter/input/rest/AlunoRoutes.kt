package adapter.input.rest

import adapter.input.ui.MainPageTemplate
import adapter.input.ui.tableAlunos
import adapter.input.ui.tableExtensao
import adapter.input.ui.title
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.appLocale
import main.currentDateTime
import main.dateTimeFormat
import model.Aluno
import model.AlunoRepository
import model.RepositoryFactory
import services.application.AlunoFilter

private data class Parameters(val sorting: String, val filter: String, val search: String)

fun Routing.alunoRoutes() {
    get("/") {
        call.respondHtmlTemplate(MainPageTemplate(), status = HttpStatusCode.OK) {
        }
    }

    get("/alunos") {
        val params = getParameters(call)
        val alunos = findAlunos(params.sorting, params.filter, params.search)

        call.respondHTML(status = HttpStatusCode.OK) {
            tableAlunos(alunos, params.sorting, params.filter, params.search)
        }
    }

    get("/alunos/download") {
        val params = getParameters(call)
        val alunos = findAlunos(params.sorting, params.filter, params.search)

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"alunos ${currentDateTime().format(dateTimeFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Sexo;Data de nascimento;Trancamento;Prazo de Extensão;Ingresso;Evasão;Data de evasão;Logradouro;Numero;Complemento;Bairro;Cidade;CEP;Telefone1;Telefone2\n" +
            alunos.joinToString(separator = "\n") { it.toDownloadAlunoCsv() }
        }
    }

    get("/alunos/extensao") {
        val alunos = findAlunos()

        call.respondHTML(status = HttpStatusCode.OK) {
            tableExtensao(alunos)
        }
    }

    get("/alunos/extensao/download") {
        val alunos = findAlunos()

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"alunos para extensao ${currentDateTime().format(dateTimeFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Período Limite;Períodos;Extensão (períodos)\n" +
                    alunos.joinToString(separator = "\n") { it.toDownloadExtensaoCsv() }
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

private fun findAlunos(): List<Aluno> =
    RepositoryFactory.get(AlunoRepository::class)
        .findByFilter(AlunoFilter.ACTIVE)
        .filter { it.ultimoPeriodoCursado.numero >= 11 }
        .sortedByDescending { it.ultimoPeriodoCursado.numero }

private fun Aluno.toDownloadAlunoCsv() = "${matricula};${nome};${versao};${email};${sexo};${dataNascimento};${trancamentos};${prazoExtensao};${ingresso};${evasao};${dataEvasao?:""};${logradouro};${numero};${complemento};${bairro};${cidade};${cep};${telefone1};${telefone2}"

private fun Aluno.toDownloadExtensaoCsv() = "${matricula};${nome};${versao};${email};${periodoLimite};${ultimoPeriodoCursado.numero};${prazoExtensao}"
