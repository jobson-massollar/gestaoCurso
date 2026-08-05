package adapter.input.ui

import adapter.input.rest.Jubilados
import adapter.input.rest.PAINEL_ALUNO_ROUTE
import kotlinx.html.*
import model.Aluno
import model.Periodo

fun FlowContent.tableJubilamentos(jubilados: Jubilados) {
    title("Jubilamentos", "/jubilamentos/download") {
        tableJubilamentoPorAbandono(jubilados.porAbandono)
        tableJubilamentoPorPrazo(jubilados.porPrazo)
    }
}

fun FlowContent.tableJubilamentoPorAbandono(jubilados: List<Aluno>) {
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +"Jubilamento por Abandono" }
        hr(classes = "border-base-content/50") { }

        if (jubilados.isEmpty()) {
            p(classes = "mt-4 mb-4 text-base") { +"Nenhum aluno a ser jubilado por abandono!"}
            return@div
        }

        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center w-1/11") { +"Matrícula" }
                    th(classes = "w-3/11") { +"Nome" }
                    th(classes = "text-center w-1/11") { +"Currículo" }
                    th(classes = "w-3/11") { +"E-mail" }
                    th(classes = "text-center w-1/11") { +"Trancamentos" }
                    th(classes = "w-2/11") { +" " }
                }
            }
            tbody {
                jubilados.forEach { aluno ->
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td(classes = "text-center") { +aluno.matricula }
                        td { +aluno.nome }
                        td(classes = "text-center") { +aluno.versao }
                        td { +aluno.email }
                        td(classes = "text-center") { +aluno.trancamentos.toString() }
                        td(classes = "gap-4") {
                            smallButton("Painel", "$PAINEL_ALUNO_ROUTE/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                            smallButton("Histórico", "/historico/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                        }
                    }
                }
            }

        }
    }
}

fun FlowContent.tableJubilamentoPorPrazo(jubilados: List<Aluno>) {
    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +"Jubilamento por Prazo" }
        hr(classes = "border-base-content/50") { }

        if (jubilados.isEmpty()) {
            p(classes = "mt-4 mb-4 text-base") { +"Nenhum aluno a ser jubilado por prazo!" }
            return@div
        }

        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center w-1/11") { +"Matrícula" }
                    th(classes = "w-3/11") { +"Nome" }
                    th(classes = "text-center w-1/11") { +"Currículo" }
                    th(classes = "w-3/11") { +"E-mail" }
                    th(classes = "text-center w-1/11") { +"Limite" }
                    th(classes = "w-2/11") { +" " }
                }
            }
            tbody {
                jubilados.forEach { aluno ->
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td(classes = "text-center") { +aluno.matricula }
                        td { +aluno.nome }
                        td(classes = "text-center") { +aluno.versao }
                        td { +aluno.email }
                        td(classes = "text-center") { +aluno.periodoLimite.toString() }
                        td(classes = "gap-4") {
                            smallButton("Painel", "$PAINEL_ALUNO_ROUTE/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                            smallButton("Histórico", "/historico/${aluno.matricula}", "#main-container", !aluno.estaAtivo)
                        }
                    }
                }
            }

        }
    }
}