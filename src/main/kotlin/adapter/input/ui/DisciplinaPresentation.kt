package adapter.input.ui

import adapter.input.rest.*
import kotlinx.html.*
import model.Disciplina

const val DISCIPLINAS_FORM = "disciplinas"

fun FlowContent.tableObrigatorias(disciplinas: List<Disciplina>, versao: String, currentSorting: String) {
    title("Disciplinas Obrigatórias - $versao", backButton = true) {

        if (disciplinas.isEmpty()) {
            p(classes = "text-base") { +"Disciplinas não encontradas!" }
            return@title
        }

        form {
            name = DISCIPLINAS_FORM
            hiddenInput {
                name = "sort"
                value = currentSorting
            }
            div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
                table(classes = "table table-zebra table-sm") {
                    thead {
                        tr(classes = "bg-base-300") {
                            th(classes = "text-center") { +"Código" }
                            th {
                                +"Nome"
                                sortingButtons(
                                    "$DISCIPLINA_ROUTE/${versao.replace('/','-')}",
                                    DISCIPLINAS_FORM,
                                    DISCIPLINA_SORTING_NOME_ASC,
                                    DISCIPLINA_SORTING_NOME_DESC
                                )
                            }
                            th(classes = "text-center") {
                                +"Aptos (não matriculados)"
                                sortingButtons(
                                    "$DISCIPLINA_ROUTE/${versao.replace('/','-')}",
                                    DISCIPLINAS_FORM,
                                    DISCIPLINA_SORTING_APTOS_ASC,
                                    DISCIPLINA_SORTING_APTOS_DESC
                                )
                            }
                            th(classes = "text-center") { +"Recusados por falta de vagas" }
                            th(classes = "text-center") { +"Matriculados" }
                            th { +" " }
                        }
                    }
                    tbody {
                        disciplinas.forEach { disciplina ->
                            tr {
                                td(classes = "text-center") { +disciplina.codigo }
                                td { +disciplina.nome }
                                td(classes = "text-center") { +disciplina.podemCursar.size.toString() }
                                td(classes = "text-center") { +disciplina.recusadosFaltaVaga.size.toString() }
                                td(classes = "text-center") { +disciplina.matriculadosCurso.size.toString() }
                                td { +"" }
                            }
                        }
                    }
                }
            }
        }
    }
}