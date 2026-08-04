package adapter.input.ui

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import model.Disciplina
import model.ItemDiario

fun FlowContent.tableDiario(disciplina: Disciplina, itensDiario: List<ItemDiario>) {
    title("${disciplina.nome} (${disciplina.codigo}) - ${itensDiario.size} aluno(s)", backButton = true) {

        if (itensDiario.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno encontrado!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th { +"#" }
                        th { +"Matricula" }
                        th { +"Nome" }
                    }
                }
                tbody {
                    itensDiario.forEachIndexed { i, item ->
                        tr {
                            td { +(i+1).toString() }
                            td { +item.matricula }
                            td { +item.nome }
                        }
                    }
                }
            }
        }
    }
}