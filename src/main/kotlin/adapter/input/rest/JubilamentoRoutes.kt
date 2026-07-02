package adapter.input.rest

import adapter.input.ui.tableJubilamentoPorAbandono
import adapter.input.ui.tableJubilamentoPorPrazo
import adapter.input.ui.tableJubilamentos
import adapter.input.ui.title
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.datetime.format
import main.currentDateTime
import main.dateTimeFormat
import model.Aluno
import model.AlunoRepository
import model.RepositoryFactory
import services.application.AlunoFilter
import kotlin.collections.joinToString

data class Jubilados(val porAbandono: List<Aluno>, val porPrazo: List<Aluno>)

fun Route.jubilamentoRoutes() {

    get("/jubilamentos") {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableJubilamentos(findJubilados())
        }
    }

    get("/jubilamentos/download") {
        val jubilados = findJubilados()

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"jubilamentos ${currentDateTime().format(dateTimeFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Tipo;Matricula;Nome;Versao;E-mail;Trancamentos;Limite\n" +
            jubilados.porAbandono.joinToString(separator = "\n") { it.toCsvAbandono() } + "\n" +
            jubilados.porPrazo.joinToString(separator = "\n") { it.toCsvPrazo() }
        }
    }
}

private fun findJubilados(): Jubilados {
    val alunos = RepositoryFactory.get(AlunoRepository::class).findByFilter(AlunoFilter.ACTIVE)

    return Jubilados (
        alunos.filter { it.jubiladoPorAbandono },
        alunos.filter { it.jubiladoPorPrazo }
    )
}

private fun Aluno.toCsv(tipoAbandono: String) = "$tipoAbandono;${this.matricula};${this.nome};${this.versao};${this.email}"

private fun Aluno.toCsvAbandono() = "${this.toCsv("Abandono")};${this.trancamentos};-"

private fun Aluno.toCsvPrazo() = "${this.toCsv("Prazo")};-;${this.periodoLimite}"