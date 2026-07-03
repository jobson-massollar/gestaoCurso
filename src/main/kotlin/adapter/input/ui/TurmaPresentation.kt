package adapter.input.ui

import kotlinx.html.*
import model.Turma

fun FlowContent.tableTurmas(turmas: List<Turma>) {
    title("Turmas") {

        if (turmas.isEmpty()) {
            p(classes = "text-base") { +"Nenhuma turma encontrada!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th(classes = "text-center") { +"Turma" }
                        th(classes = "text-center") { +"Versão" }
                        th(classes = "text-center") { +"Disciplina" }
                        th { +"Nome" }
                        th(classes = "text-center") { +"Inscritos" }
                        th(classes = "text-center") { +"Inscritos" }
                        th { +" " }
                    }
                }
                tbody {
                    turmas.forEach { turma ->
                        turma.disciplinas.forEachIndexed { index, disciplina ->
                            tr {
                                if (index == 0) {
                                    td(classes = "text-center") {
                                        rowSpan = turma.disciplinas.size.toString()
                                        +turma.codigo
                                    }
                                }

                                td(classes = "text-center") { +disciplina.versao }
                                td(classes = "text-center") { +disciplina.codigo }
                                td { +disciplina.nome }
                                td(classes = "text-center") { +disciplina.inscritos.toString() }

                                if (index == 0) {
                                    td(classes = "text-center") {
                                        rowSpan = turma.disciplinas.size.toString()
                                        +turma.inscritos.toString()
                                    }
                                }

                                td {
                                    smallButton(
                                        "Alunos",
                                        $"/diario/${disciplina.versao}/${disciplina.codigo}",
                                        "#main-container"
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