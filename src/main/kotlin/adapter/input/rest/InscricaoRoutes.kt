package adapter.input.rest

import adapter.input.ui.observacoes
import adapter.input.ui.tableInscricoes
import adapter.input.ui.tableInscricoesIrregulares
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.currentDateTime
import main.dateTimeFormat
import model.Aluno
import model.AlunoRepository
import model.RepositoryFactory
import model.TotalizacaoInscricaoRepository
import services.application.AlunoFilter
import services.application.InscricaoSorting

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
            tableInscricoes(totalizacoes.sortedWith(comparator), sorting)
        }
    }

    get(DOWNLOAD_INSCRICOES_ROUTE) {

    }

    get(INSCRICOES_IRREGULARES_ROUTE) {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableInscricoesIrregulares(findAlunosIrregulares())
        }
    }

    get(DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE) {

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"inscricoes-irregulares ${currentDateTime().format(dateTimeFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail;Inscricoes;Observacoes\n" +
            findAlunosIrregulares().joinToString(separator = "\n") { it.toCsv() + it.observacoes.joinToString(" / ") }
        }
    }
}

private fun findAlunosIrregulares() =
    RepositoryFactory.get(AlunoRepository::class)
        .findByFilter(AlunoFilter.ACTIVE)
        .filter { it.estaIrregular }
        .sortedBy { it.itensMatriculados.size }

private fun Aluno.toCsv() = "${this.matricula};${this.nome};${this.versao};${this.email};${itensMatriculados.size};"