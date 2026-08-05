package adapter.input.ui

import adapter.input.rest.DOWNLOAD_ALUNOS_EXTENSAO_ROUTE
import adapter.input.rest.PAINEL_ALUNO_ROUTE
import kotlinx.html.*
import model.Aluno
import model.Periodo

fun FlowContent.tableExtensao(alunos: List<Aluno>) {
    title("Alunos com 11 ou mais Períodos de Integralização", DOWNLOAD_ALUNOS_EXTENSAO_ROUTE) {

        if (alunos.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno em período de extensão" }
            return@title
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
                            +"(períodos)"
                        }
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
                                smallButton(
                                    "Painel",
                                    "$PAINEL_ALUNO_ROUTE/${aluno.matricula}",
                                    "#main-container",
                                    !aluno.estaAtivo
                                )
                                smallButton(
                                    "Histórico",
                                    $"/historico/${aluno.matricula}",
                                    "#main-container",
                                    !aluno.estaAtivo
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
