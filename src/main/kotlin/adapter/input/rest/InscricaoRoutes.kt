package adapter.input.rest

import adapter.input.ui.observacoes
import adapter.input.ui.tableInscricoes
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

fun Route.inscricoesRoutes() {

    get("/inscricoes/irregulares") {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableInscricoes(findAlunos())
        }
    }
    get("/inscricoes/irregulares/download") {

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"inscricoes-irregulares ${currentDateTime().format(dateTimeFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Inscricoes;Observacoes\n" +
            findAlunos().joinToString(separator = "\n") { it.toCsv() + it.observacoes.joinToString(" / ") }
        }
    }
}

private fun findAlunos() =
    RepositoryFactory.get(AlunoRepository::class)
        .findByFilter(AlunoFilter.ACTIVE)
        .filter { it.estaIrregular }
        .sortedBy { it.itensMatriculados.size }

private fun Aluno.toCsv() = "${this.matricula};${this.nome};${this.versao};${this.email};${itensMatriculados.size};"