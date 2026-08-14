package adapter.input.ui

import adapter.input.rest.DOWNLOAD_DIARIO_ROUTE
import adapter.input.rest.HISTORICO_ROUTE
import adapter.input.rest.PAINEL_ALUNO_ROUTE
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
import model.Turma
import model.ehAlunoBSI

fun FlowContent.tableDiario(turma: Turma, disciplina: Disciplina, itensDiario: List<ItemDiario>) {
    title("Diário - ${disciplina.codigo} - ${disciplina.nome} (${turma.codigo}) - ${itensDiario.size} aluno(s)", backButton = true, downloadURL = "$DOWNLOAD_DIARIO_ROUTE/${turma.codigo}/${
        disciplina.versao.replace(
            '/',
            '-'
        )
    }/${disciplina.codigo}") {

        if (itensDiario.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno encontrado!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th { +"#" }
                        th(classes = "text-center") { +"Matricula" }
                        th { +"Nome" }
                        th(classes = "text-center") { +"Currículo" }
                        th { +"E-mail" }
                        th { +" "}
                    }
                }
                tbody {
                    itensDiario.forEachIndexed { i, item ->
                        tr {
                            td { +(i+1).toString() }
                            td(classes = "text-center") { +item.matricula }
                            td { +item.nome }
                            td(classes = "text-center") { +(item.aluno?.let { "${it.versao}" } ?: "-") }
                            td { +(item.aluno?.let { "${it.email}" } ?: "-") }
                            td(classes = "gap-4") {
                                smallButton(
                                    "Painel",
                                    "$PAINEL_ALUNO_ROUTE/${item.matricula}",
                                    "#main-container",
                                    ! item.matricula.ehAlunoBSI
                                )
                                smallButton(
                                    "Histórico",
                                    "$HISTORICO_ROUTE/${item.matricula}",
                                    "#main-container",
                                    ! item.matricula.ehAlunoBSI
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}