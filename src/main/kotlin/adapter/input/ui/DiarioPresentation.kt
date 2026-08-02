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
    title("Diário - ${disciplina.nome} (${disciplina.codigo})") {

        if (itensDiario.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno encontrado!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th { +"Matricula" }
                        th { +"Nome" }
                    }
                }
                tbody {
                    itensDiario.forEach { item ->
                        tr {
                            td { +item.matricula }
                            td { +item.nome }
                        }
                    }
                }
            }
        }
    }
}