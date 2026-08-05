package adapter.input.ui

import adapter.input.rest.*
import io.ktor.htmx.html.*
import kotlinx.html.*
import model.Aluno

const val ALUNOS_FORM = "alunos"

fun FlowContent.tableAlunos(alunos: List<Aluno>, currentSorting: String, currentFilter: String, search: String) {
    title(
        "Alunos",
        "$DOWNLOAD_ALUNOS_ROUTE?sort=${currentSorting}&filter=${currentFilter}&search=${search}"
    ) {
        form {
            name = ALUNOS_FORM
            hiddenInput {
                name = "sort"
                value = currentSorting
            }
            div(classes = "mb-4 shadow-sm rounded-box border border-base-content/50 bg-base-200 flex items-center") {
                label(classes = "ml-4 mt-2 mb-2") { +"Alunos:" }
                radioButton(
                    "filter",
                    ALUNOS_ROUTE,
                    "#main-container",
                    currentFilter,
                    ALUNO_FILTER_ALL
                )
                label(classes = "ml-2") { +"Todos" }
                radioButton(
                    "filter",
                    ALUNOS_ROUTE,
                    "#main-container",
                    currentFilter,
                    ALUNO_FILTER_ACTIVE
                )
                label(classes = "ml-2") { +"Ativos" }
                radioButton(
                    "filter",
                    ALUNOS_ROUTE,
                    "#main-container",
                    currentFilter,
                    ALUNO_FILTER_GRADUATING
                )
                label(classes = "ml-2") { +"Formandos" }
                radioButton(
                    "filter",
                    ALUNOS_ROUTE,
                    "#main-container",
                    currentFilter,
                    ALUNO_FILTER_GRADUATED
                )
                label(classes = "ml-2") { +"Formados" }
                label(classes = "ml-10") { +"Filtro" }
                searchInput(classes = "ml-2") {
                    attributes.hx {
                        get = ALUNOS_ROUTE
                        target = "#main-container"
                        swap = "innerHTML"
                        include = "closest form"
                        trigger = "input delay:700ms changed"
                        indicator = "#loading-spinner"
                        replaceUrl = "true"
                    }
                    name = "search"
                    size = "15"
                    maxLength = "15"
                    value = search
                    autoFocus = true
                    onFocus = "this.selectionStart = this.selectionEnd = this.value.length;"
                }
                label(classes = "ml-auto mr-4") { +"Total: ${alunos.size}" }
            }
            if (alunos.isEmpty()) {
                p(classes = "text-base") { +"Nenhum aluno encontrado!" }
                return@form
            }
            div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
                if (alunos.isEmpty()) {
                    p { +"Nenhum aluno encontrado!" }
                }
                table(classes = "table table-zebra table-sm") {
                    thead {
                        tr(classes = "bg-base-300") {
                            th(classes = "gap-4 text-center") {
                                +"Matrícula"
                                sortingButtons(ALUNOS_ROUTE, ALUNOS_FORM, ALUNO_SORTING_MATRICULA_ASC, ALUNO_SORTING_MATRICULA_DESC)
                            }
                            th(classes = "gap-4") {
                                +"Nome"
                                sortingButtons(ALUNOS_ROUTE, ALUNOS_FORM,ALUNO_SORTING_NOME_ASC, ALUNO_SORTING_NOME_DESC)
                            }
                            th(classes = "text-center") { +"Currículo" }
                            th { +"E-mail" }
                            if (currentFilter == ALUNO_FILTER_ALL) {
                                th { +"Evasão" }
                            }
                            th { +" " }
                        }
                    }
                    alunos.forEach { aluno ->
                        tr(classes = "hover:bg-secondary hover:text-base-100") {
                            td(classes = "text-center") { +aluno.matricula }
                            td { +aluno.nome }
                            td(classes = "text-center") { +aluno.versao }
                            td { +aluno.email }
                            if (currentFilter == ALUNO_FILTER_ALL) {
                                td { +aluno.evasao.take(40) }
                            }
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

fun FlowContent.tableColacao(alunos: List<Aluno>) {
    title("Alunos aptos para Colação de Grau") {

        if (alunos.isEmpty()) {
            p(classes = "text-base") { +"Nenhum aluno apto para colação de grau" }
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
                        th { +" " }
                    }
                }
                tbody {
                    alunos.forEach { aluno ->
                        tr {
                            td(classes = "text-center") { +aluno.matricula }
                            td { +aluno.nome }
                            td(classes = "text-center") { +aluno.versao }
                            td { +aluno.email }
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
