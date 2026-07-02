package adapter.input.ui

import kotlinx.html.FlowContent
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import model.Aluno
import model.Periodo
import kotlin.collections.forEach

fun FlowContent.tableExtensao(alunos: List<Aluno>) {
    if (alunos.isEmpty()) {
        p(classes = "text-base") { +"Nenhum aluno em período de extensão"}
        return
    }

    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center") { +"Matrícula" }
                    th { +"Nome" }
                    th(classes = "text-center") { +"Currículo" }
                    th { +"E-mail" }
                    th(classes = "text-center") { +"Limite" }
                    th(classes = "text-center") { +"Períodos" }
                    th(classes = "text-center") {
                        +"Extensão"
                        br()
                        +"(períodos)" }
                    th { +" " }
                }
            }
            tbody {
                alunos.forEach { aluno ->
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td(classes = "text-center") { +aluno.matricula }
                        td { +aluno.nome }
                        td(classes = "text-center") { +aluno.versao }
                        td { +aluno.email }
                        td(classes = "text-center") { +(aluno.periodoLimite.toString() + if (aluno.periodoLimite < Periodo.ATUAL) " 🔴" else "") }
                        td(classes = "text-center") { +aluno.ultimoPeriodoCursado.numero.toString() }
                        td(classes = "text-center") { +aluno.prazoExtensao.toString() }
                        td(classes = "gap-4") {
                            smallButton("Painel", $"/alunos/painel/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                            smallButton("Histórico", $"/historico/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                        }
                    }
                }
            }
        }
    }
}
