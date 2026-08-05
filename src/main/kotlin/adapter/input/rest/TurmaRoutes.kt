package adapter.input.rest

import adapter.input.ui.tableTurmas
import io.ktor.http.*
import io.ktor.server.routing.*
import main.collator
import model.RepositoryFactory
import model.TurmaRepository

const val TURMAS_ROUTE = "/turmas"

fun Routing.turmaRoutes() {

    get(TURMAS_ROUTE) {
        val turmas = RepositoryFactory.get(TurmaRepository::class).findAll()

        turmas.forEach { turma ->
            turma.disciplinas.sortByDescending { disciplina -> disciplina.versao }
        }


        call.respondHTML(HttpStatusCode.OK) {
            tableTurmas(
                turmas.sortedWith(compareBy(collator) {
                    it.disciplinas.first().nome
                })
            )
        }
    }
}