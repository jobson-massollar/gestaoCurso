package adapter.input.rest

import adapter.input.ui.tableJubilamentoPorAbandono
import adapter.input.ui.tableJubilamentoPorPrazo
import adapter.input.ui.title
import io.ktor.http.HttpStatusCode
import io.ktor.server.html.respondHtmlFragment
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import model.AlunoRepository
import model.RepositoryFactory
import services.application.AlunoFilter

fun Route.jubilamentoRoutes() {

    get("/jubilamentos") {
        val alunos = RepositoryFactory.get(AlunoRepository::class).findByFilter(AlunoFilter.ACTIVE)
        val jubiladosPorAbandono = alunos.filter { it.jubiladoPorAbandono }
        val jubiladosPorPrazo = alunos.filter { it.jubiladoPorPrazo }

        call.respondHtmlFragment(status = HttpStatusCode.OK) {
            title("Jubilamentos") {
                tableJubilamentoPorAbandono(jubiladosPorAbandono)
                tableJubilamentoPorPrazo(jubiladosPorPrazo)
            }
        }
    }
}