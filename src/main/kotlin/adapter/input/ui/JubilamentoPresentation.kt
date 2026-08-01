package adapter.input.ui

import adapter.input.rest.Jubilados
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
                jubilados.forEach {
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td(classes = "text-center") { +it.matricula }
                        td { +it.nome }
                        td(classes = "text-center") { +it.versao }
                        td { +it.email }
                        td(classes = "text-center") { +it.trancamentos.toString() }
                        td(classes = "gap-4") {
                            smallButton("Painel", $"/alunos/painel/${it.matricula}", "#main-container", !it.estaAtivo)
                            smallButton("Histórico", $"/historico/${it.matricula}", "#main-container", !it.estaAtivo)
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
                jubilados.forEach {
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td(classes = "text-center") { +it.matricula }
                        td { +it.nome }
                        td(classes = "text-center") { +it.versao }
                        td { +it.email }
                        td(classes = "text-center") { +it.periodoLimite.toString() }
                        td(classes = "gap-4") {
                            smallButton("Painel", $"/alunos/painel/${it.matricula}", "#main-container", !it.estaAtivo)
                            smallButton("Histórico", $"/historico/${it.matricula}", "#main-container", !it.estaAtivo)
                        }
                    }
                }
            }

        }
    }
}