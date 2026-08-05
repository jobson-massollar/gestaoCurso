package adapter.input.rest

import adapter.input.ui.observacoes
import adapter.input.ui.tableInscricoesAluno
import adapter.input.ui.tableTotalizacaoInscricoes
import adapter.input.ui.tableInscricoesDisciplina
import adapter.input.ui.tableInscricoesIrregulares
import io.ktor.http.*
import io.ktor.server.html.respondHtml
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.currentDateTime
import main.fileTimestampFormat
import model.Aluno
import model.AlunoRepository
import model.InscricaoRepository
import model.RepositoryFactory
import model.TotalizacaoInscricaoRepository
import services.application.AlunoFilter

const val INSCRICOES_ROUTE = "/inscricoes"
const val DOWNLOAD_INSCRICOES_ROUTE = "/inscricoes/download"
const val INSCRICOES_IRREGULARES_ROUTE = "/inscricoes/irregulares"
const val DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE = "/inscricoes/irregulares/download"

fun Route.inscricoesRoutes() {

    get(INSCRICOES_ROUTE) {
        val sorting = call.request.queryParameters["sort"]?: ""
        val comparator = getInscricaoSortingByValue(sorting).comparator
        val totalizacoes = RepositoryFactory.get(TotalizacaoInscricaoRepository::class).findTotalizacoes()

        call.respondHTML(status = HttpStatusCode.OK) {
            tableTotalizacaoInscricoes(totalizacoes.sortedWith(comparator), sorting)
        }
    }

    get(DOWNLOAD_INSCRICOES_ROUTE) {

    }

    get("$INSCRICOES_ROUTE/{matricula}") {
        val matricula = call.parameters["matricula"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) { }

        val aluno = RepositoryFactory.get(AlunoRepository::class).findByMatricula(matricula)

        if (aluno != null) {
            call.respondHTML(status = HttpStatusCode.OK) {
                tableInscricoesAluno(aluno)
            }
        }
        else
            call.respondHtml(HttpStatusCode.NotFound) {}
    }

    get("$INSCRICOES_ROUTE/{codigo}/{turma}") {
        val codigo = call.parameters["codigo"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) { }
        val turma = call.parameters["turma"] ?: return@get call.respondHtml(HttpStatusCode.BadRequest) { }
        val inscricoes = RepositoryFactory.get(InscricaoRepository::class).findByDisciplina(codigo, turma).sortedBy { it.nomeAluno }

        call.respondHTML(status = HttpStatusCode.OK) {
            tableInscricoesDisciplina(inscricoes, codigo, turma)
        }
    }

    get(INSCRICOES_IRREGULARES_ROUTE) {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableInscricoesIrregulares(findAlunosIrregulares())
        }
    }

    get(DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE) {

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"inscricoes-irregulares ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Matriculado;Trancamentos;Observacoes\n" +
            findAlunosIrregulares().joinToString(separator = "\n") { it.toCsv() + it.observacoes.joinToString(" / ") }
        }
    }
}

private fun findAlunosIrregulares() =
    RepositoryFactory.get(AlunoRepository::class)
        .findInscricoesIrregulares()
        .filter { it.estaIrregular }
        .sortedBy { it.itensMatriculados.size }

private fun Aluno.toCsv() = "${this.matricula};${this.nome};${this.versao};${this.email};${this.itensMatriculados.size};${this.trancamentos};"