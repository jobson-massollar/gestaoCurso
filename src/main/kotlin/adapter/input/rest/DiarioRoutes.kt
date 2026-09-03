package adapter.input.rest

import adapter.input.ui.tableDiario
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.collator
import main.currentDateTime
import main.fileTimestampFormat
import model.ItemDiario
import model.RepositoryFactory
import model.TurmaRepository

const val DIARIO_ROUTE = "/diario"
const val DOWNLOAD_DIARIO_ROUTE = "/diario/download"

private data class DiarioParameters(val codigoTurma: String, val versao: String, val codigoDisciplina: String)

fun Route.diarioRoutes() {

    get("$DIARIO_ROUTE/{turma}/{versao}/{codigo}") {
        val params = call.getDiarioParameters()

        val turma =
            RepositoryFactory.get(TurmaRepository::class).findByCode(params.codigoTurma) ?: return@get call.respondBadRequest()

        val disciplina = turma.getDisciplina(params.versao, params.codigoDisciplina) ?: return@get call.respondBadRequest()

        val itensDiario = turma.itensDiario(disciplina)

        call.respondHTML(HttpStatusCode.OK) {
            tableDiario(
                turma,
                disciplina,
                itensDiario.sortedWith(compareBy(collator) {
                    it.nome
                })
            )
        }
    }

    get("$DOWNLOAD_DIARIO_ROUTE/{turma}/{versao}/{codigo}") {
        val params = call.getDiarioParameters()

        val turma =
            RepositoryFactory.get(TurmaRepository::class).findByCode(params.codigoTurma) ?: return@get call.respondBadRequest()

        val disciplina = turma.getDisciplina(params.versao, params.codigoDisciplina) ?: return@get call.respondBadRequest()

        val itensDiario = turma.itensDiario(disciplina)

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"diario ${disciplina.codigo}-${disciplina.nome} ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Matricula;Nome;Versao;E-mail\n" +
                    itensDiario.joinToString(separator = "\n") { it.toCsv() }
        }
    }
}

private fun RoutingCall.getDiarioParameters(): DiarioParameters =
    DiarioParameters(
        this.parameters["turma"]?:"",
        (this.parameters["versao"]?:"").replace('-', '/'),
        this.parameters["codigo"]?:""
    )

private fun ItemDiario.toCsv() = "${this.matricula};${this.nome};${this.aluno?.let { "${it.versao}" } ?: "-"};${this.aluno?.let { "${it.email}" } ?: "-"}"