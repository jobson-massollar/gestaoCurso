package adapter.input.ui

import kotlinx.html.*
import model.Aluno
import model.COMPLEMENTAR
import model.ELETIVA
import model.ItemHistorico
import model.OBRIGATORIA
import model.OPTATIVA
import kotlin.collections.iterator

private fun FlowContent.disciplinas(historico: List<ItemHistorico>) {
    if (historico.isEmpty()) return
    div(classes = "border border-base-content/50 bg-base-100 mb-4") {
        table(classes = "table table-zebra table-sm") {
            thead {
                tr {
                    th { +"Semestre" }
                    th { +"Código" }
                    th { +"Nome" }
                    th { +"Situação" }
                    th { +"Nota" }
                    th { +"Horas" }
                }
            }
            tbody {
                historico
                    .forEach {
                        tr {
                            td {
                                +"${it.ano}.${it.periodo}"
                            }
                            td {
                                +it.codigo
                            }
                            td {
                                +it.nome
                            }
                            td {
                                +it.descricao.take(3)
                            }
                            td {
                                +"${it.nota?.format(2) ?: ""}"
                            }
                            td {
                                +"${it.horas}"
                            }
                        }
                    }
            }
        }
    }
}

fun FlowContent.historicoAluno(aluno: Aluno, historico: List<ItemHistorico>) {

    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        disciplinas(historico.filter  { it.tipo == OBRIGATORIA })
        disciplinas(historico.filter  { it.tipo == OPTATIVA })
        disciplinas(historico.filter  { it.tipo == COMPLEMENTAR })
        disciplinas(historico.filter  { it.tipo == ELETIVA })
    }

    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "border m-4 text-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    for (s in aluno.semestreInicial..aluno.semestreFinal) {
                        th(classes = "border text-center pl-2 pr-2") { +s.toString() }
                    }
                }
            }
            tbody {
                tr {
                    (aluno.semestreInicial..aluno.semestreFinal).forEach {
                        td(classes = "border text-center") { +"todo" }
                    }
                }
            }
        }
    }
}