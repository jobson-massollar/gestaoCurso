package adapter.input.ui

import kotlinx.html.*
import model.Aluno
import model.COMPLEMENTAR
import model.ELETIVA
import model.ItemHistorico
import model.OBRIGATORIA
import model.OPTATIVA
import model.Semestre
import kotlin.collections.iterator

private fun FlowContent.disciplinas(title: String, historico: List<ItemHistorico>) {
    if (historico.isEmpty()) return
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +title }
        hr(classes = "border-base-content/50") {  }
        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
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

fun FlowContent.historicoAluno(aluno: Aluno, historico: List<ItemHistorico>, hisAprovados: List<ItemHistorico>) {

//    div(classes = "card bg-base-100 overflow-x-auto shadow-sm") {
//        div(classes = "card-body") {
//            h2(classes = "card-title") { +"Obrigatórias"}
//
//        }
//    }
    //div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        disciplinas("Obrigatórias", hisAprovados.filter  { it.tipo == OBRIGATORIA })
        disciplinas("Optativas", hisAprovados.filter  { it.tipo == OPTATIVA })
        disciplinas("Complementares", hisAprovados.filter  { it.tipo == COMPLEMENTAR })
        disciplinas("Eletivas", hisAprovados.filter  { it.tipo == ELETIVA })
    //}

    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "table") {
            thead {
                tr(classes = "bg-base-300") {
                    for (s in aluno.semestreInicial..aluno.semestreFinal) {
                        th(classes = "text-center pl-2 pr-2") { +s.toString() }
                    }
                }
            }
            tbody {
                tr {
                    for (s in aluno.semestreInicial..aluno.semestreFinal) {
                        val label = situacaoSemestre(s, historico.filter { it.ano == s.ano && it.periodo == s.semestre })
                        td(classes = "text-center ${if (label == "T") "bg-red-400" else ""}") { +label }
                    }
                }
            }
        }
    }
}

private fun situacaoSemestre(s: Semestre, historico: List<ItemHistorico>): String {
    if (historico.isEmpty()) return "-"
    if (historico[0].isTrancamento) return "T"

    var m = 0
    var a = 0
    var r = 0

    historico.forEach {
        if (it.isAprovado) a++
        else if (it.isReprovado) r++
        else if (it.isMatriculado) m++
    }
    return buildString {
        var s = ""
        if (m > 0) {
            append("${m}M")
            s = "/"
        }
        if (a > 0) {
            append("${s}${a}A")
            s = "/"
        }
        if (r > 0) append("${s}${r}R")
    }
}