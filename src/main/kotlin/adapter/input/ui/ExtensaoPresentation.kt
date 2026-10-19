package adapter.input.ui

import adapter.input.rest.ALUNOS_EXTENSAO_ROUTE
import adapter.input.rest.ALUNOS_ROUTE
import adapter.input.rest.ALUNO_FILTER_ACTIVE
import adapter.input.rest.ALUNO_FILTER_ALL
import adapter.input.rest.ALUNO_FILTER_GRADUATED
import adapter.input.rest.ALUNO_FILTER_GRADUATING
import adapter.input.rest.DOWNLOAD_ALUNOS_EXTENSAO_ROUTE
import adapter.input.rest.HISTORICO_ROUTE
import adapter.input.rest.PAINEL_ALUNO_ROUTE
import io.ktor.htmx.html.hx
import kotlinx.html.*
import model.Aluno
import model.Periodo

fun FlowContent.tableExtensao(alunos: List<Aluno>, periodo: Int) {
    title("Alunos com $periodo ou mais Períodos de Integralização", DOWNLOAD_ALUNOS_EXTENSAO_ROUTE) {

        if (alunos.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno em período de extensão" }
            return@title
        }

        form {
            div(classes = "mb-4 shadow-sm rounded-box border border-base-content/50 bg-base-200 flex items-center") {
                label(classes = "ml-4 mt-2 mb-2") { +"Períodos:" }
                input(classes = "input validator w-15 ml-2 mt-2 mb-2") {
                    attributes.hx {
                        get = ALUNOS_EXTENSAO_ROUTE
                        target = "#main-container"
                        swap = "innerHTML"
                        indicator = "#loading-spinner"
                        include = "closest form"
                        replaceUrl = "true"
                        validate = true
                    }
                    type = InputType.number
                    name = "semester"
                    value = periodo.toString()
                    min = "9"
                    max = "11"
                    required = true
                }
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
                                        "$HISTORICO_ROUTE/${aluno.matricula}",
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
}
