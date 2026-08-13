package adapter.input.rest

import adapter.input.ui.tableSituacaoIrregular
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.collator
import main.currentDateTime
import main.fileTimestampFormat
import model.Aluno
import model.AlunoRepository
import model.RepositoryFactory
import model.trancados
import services.application.AlunoFilter

const val SITUACAO_IRREGULAR_ROUTE = "/jubilamentos"
const val DOWNLOAD_SITUACAO_IRREGULAR_ROUTE = "/jubilamentos/download"

data class SituacaoIrregular(val porAbandono: List<Aluno>, val porPrazo: List<Aluno>)

fun Route.situacaoIrregularRoutes() {

    get(SITUACAO_IRREGULAR_ROUTE) {
        call.respondHTML(status = HttpStatusCode.OK) {
            tableSituacaoIrregular(findAlunosSituacaoIrregular())
        }
    }

    get(DOWNLOAD_SITUACAO_IRREGULAR_ROUTE) {
        val alunosSituacaoIrregular = findAlunosSituacaoIrregular()

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"situacao-irregular ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Tipo;Matricula;Nome;Versao;E-mail;Trancamentos;Trancados;Limite\n" +
            alunosSituacaoIrregular.porAbandono.joinToString(separator = "\n") { it.toCsvAbandono() } + "\n" +
            alunosSituacaoIrregular.porPrazo.joinToString(separator = "\n") { it.toCsvPrazo() }
        }
    }
}

private fun findAlunosSituacaoIrregular(): SituacaoIrregular {
    val alunos = RepositoryFactory.get(AlunoRepository::class).findByFilter(AlunoFilter.ACTIVE)

    return SituacaoIrregular (
        alunos.filter { it.irregularPorAbandono }.sortedWith { a1, a2 -> collator.compare(a1.nome, a2.nome) },
        alunos.filter { it.irregularPorPrazo }.sortedWith { a1, a2 -> collator.compare(a1.nome, a2.nome) }
    )
}

private fun Aluno.toCsv(tipoAbandono: String) = "$tipoAbandono;${this.matricula};${this.nome};${this.versao};${this.email}"

private fun Aluno.toCsvAbandono() = "${this.toCsv("Abandono")};${this.trancamentos};${this.historico.trancados.joinToString(" / ")};-"

private fun Aluno.toCsvPrazo() = "${this.toCsv("Prazo")};-;-;${this.periodoLimite}"