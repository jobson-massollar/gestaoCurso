package adapter.input.rest

import adapter.input.ui.tableTurmas
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.format
import main.collator
import main.currentDateTime
import main.fileTimestampFormat
import model.Disciplina
import model.RepositoryFactory
import model.Turma
import model.TurmaRepository

const val TURMAS_ROUTE = "/turmas"
const val DOWNLOAD_TURMAS_ROUTE = "/turmas/download"

fun Routing.turmaRoutes() {

    get(TURMAS_ROUTE) {
        call.respondHTML(HttpStatusCode.OK) {
            tableTurmas(findTurmas())
        }
    }

    get(DOWNLOAD_TURMAS_ROUTE) {

        call.response.headers.apply {
            append("Content-Disposition", "attachment; filename=\"turmas ${currentDateTime().format(fileTimestampFormat)}.csv\"")
        }

        call.respondText(ContentType.Text.CSV, HttpStatusCode.OK) {
            "Turma;Versão;Codigo;Disciplina;Inscritos;Total\n" +
                    findTurmas().joinToString(separator = "\n") { turma ->
                        var index = 0
                        turma.disciplinas.joinToString(separator = "\n") { disciplina ->
                            disciplina.toCsv(index++, turma)
                        }
                    }
        }
    }
}

private fun findTurmas() : List<Turma> {
    val turmas = RepositoryFactory.get(TurmaRepository::class).findAll()

    turmas.forEach { turma ->
        turma.disciplinas.sortByDescending { disciplina -> disciplina.versao }
    }

    return turmas.sortedWith(compareBy(collator) {
        it.disciplinas.first().nome
    })
}

private fun Disciplina.toCsv(i: Int, turma: Turma) = "${if (i == 0) turma.codigo else ""};${this.versao};${this.codigo};${this.nome};${this.inscritos};${if (i == 0) turma.inscritos else ""}"