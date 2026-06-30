package adapter.input.ui

import adapter.input.rest.*
import io.ktor.htmx.*
import io.ktor.htmx.html.*
import kotlinx.html.*
import model.Aluno

private fun FlowOrPhrasingContent.sortingButton(sorting: String, container: String, svg: String) {
    button(classes = "btn btn-ghost btn-xs ml-2 pl-px pr-px base-content") {
        attributes.hx {
            get = "/alunos"
            target = container
            swap = HxSwap.innerHtml
            indicator = "#loading-spinner"
            include = "closest form"
        }
        onClick = "document.alunos.sort.value = '$sorting'"
        unsafe { +svg }
    }
}

private fun FlowOrPhrasingContent.sortingButtons(fieldAsc: String, fieldDesc: String) {
    sortingButton(fieldAsc, "#main-container", AZ_SORT_SVG)
    sortingButton(fieldDesc, "#main-container",ZA_SORT_SVG)
}

fun FlowContent.tableAlunos(alunos: List<Aluno>, currentSorting: String, currentFilter: String, search: String) {
    form {
        name = "alunos"
        hiddenInput {
            name = "sort"
            value = currentSorting
        }
        div(classes = "mb-4 shadow-sm rounded-box border border-base-content/50 bg-base-200 flex items-center") {
            label(classes = "ml-4 mt-2 mb-2") { +"Alunos:" }
            radioButton(
                "filter",
                "/alunos",
                "#main-container",
                currentFilter,
                ALUNO_FILTER_ALL
            )
            label(classes = "ml-2") { +"Todos" }
            radioButton(
                "filter",
                "/alunos",
                "#main-container",
                currentFilter,
                ALUNO_FILTER_ACTIVE
            )
            label(classes = "ml-2") { +"Ativos" }
            radioButton(
                "filter",
                "/alunos",
                "#main-container",
                currentFilter,
                ALUNO_FILTER_GRADUATING
            )
            label(classes = "ml-2") { +"Formandos" }
            radioButton(
                "filter",
                "/alunos",
                "#main-container",
                currentFilter,
                ALUNO_FILTER_GRADUATED
            )
            label(classes = "ml-2") { +"Formados" }
            label(classes = "ml-10") { +"Filtro" }
            searchInput(classes = "ml-2") {
                attributes.hx {
                    get = "/alunos"
                    target = "#main-container"
                    swap = HxSwap.innerHtml
                    include = "closest form"
                    trigger = "search, change, keyup delay:700ms changed"
                    indicator = "#loading-spinner"
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
            p(classes = "text-base") { +"Nenhum aluno encontrado!"}
            return@form
        }
        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            if (alunos.isEmpty()) {
                p { +"Nenhum aluno encontrado!"}
            }
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th(classes = "gap-4") {
                            +"Matrícula"
                            sortingButtons(ALUNO_SORTING_MATRICULA_ASC, ALUNO_SORTING_MATRICULA_DESC)
                        }
                        th(classes = "gap-4") {
                            +"Nome"
                            sortingButtons(ALUNO_SORTING_NOME_ASC, ALUNO_SORTING_NOME_DESC)
                        }
                        th { +"Currículo" }
                        th { +"E-mail" }
                        if (currentFilter == ALUNO_FILTER_ALL) {
                            th { +"Evasão" }
                        }
                        th { +" " }
                    }
                }
                alunos.forEach {
                    tr(classes = "hover:bg-secondary hover:text-base-100") {
                        td { +it.matricula }
                        td { +it.nome }
                        td { +it.versao }
                        td { +it.email }
                        if (currentFilter == ALUNO_FILTER_ALL) {
                            td { +it.evasao.take(40) }
                        }
                        td(classes = "gap-4") {
                            smallButton("Painel", $"/alunos/painel/${it.matricula}", "#main-container", !it.isAtivo)
                            smallButton("Histórico", $"/historico/${it.matricula}", "#main-container", !it.isAtivo)
                        }
                    }
                }
            }
        }
    }
}
