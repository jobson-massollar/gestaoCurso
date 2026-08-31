package adapter.input.ui

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import model.Disciplina

fun FlowContent.tableObrigatorias(versao: String, disciplinas: List<Disciplina>) {
    title("Disciplinas Obrigatórias - $versao", backButton = true) {

        if (disciplinas.isEmpty()) {
            p(classes = "text-base") { +"Disciplinas não encontradas!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th(classes = "text-center") { +"Código" }
                        th { +"Nome" }
                        th(classes = "text-center") { +"Aptos (não matriculados)" }
                        th(classes = "text-center") { +"Recusados por falta de vagas" }
                        th(classes = "text-center") { +"Matriculados" }
                        th { +" " }
                    }
                }
                tbody {
                    disciplinas.forEach { disciplina ->
                        tr {
                            td(classes = "text-center") { +disciplina.codigo }
                            td { +disciplina.nome }
                            td(classes = "text-center") { +disciplina.podemCursar.size.toString() }
                            td(classes = "text-center") { +disciplina.recusadosFaltaVaga.size.toString() }
                            td(classes = "text-center") { +disciplina.matriculadosCurso.size.toString() }
                            td { +""}
                        }
                    }
                }
            }
        }
    }
}