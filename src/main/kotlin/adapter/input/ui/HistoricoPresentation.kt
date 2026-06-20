package adapter.input.ui

import kotlinx.html.*
import model.Aluno
import model.COMPLEMENTAR
import model.Disciplina
import model.ELETIVA
import model.FIM_PANDEMIA
import model.INICIO_PANDEMIA
import model.ItemHistorico
import model.MATRICULADO
import model.OBRIGATORIA
import model.OPTATIVA
import model.Semestre

fun FlowContent.historicoAluno(aluno: Aluno,
                               historico: List<ItemHistorico>,
                               aprovadas: List<ItemHistorico>,
                               matriculadas: List<ItemHistorico>,
                               obrigatoriasFaltantes: List<Disciplina>) {

    val aprovadasObrigatorias = aprovadas.filter  { it.tipo == OBRIGATORIA }
    val aprovadasOptativas = aprovadas.filter  { it.tipo == OPTATIVA }
    val aprovadasComplementares = aprovadas.filter  { it.tipo == COMPLEMENTAR }
    val aprovadasEletivas = aprovadas.filter  { it.tipo == ELETIVA }

    cardDadosAluno(aluno)

    cardSemestres(aluno, historico)

    tableDisciplinas("Obrigatórias que Faltam (${obrigatoriasFaltantes.size})", obrigatoriasFaltantes)

    tableHistorico("Matriculadas (${matriculadas.size})", matriculadas)

    tableHistorico("Obrigatórias Cursadas (${aprovadasObrigatorias.size})", aprovadasObrigatorias)

    tableHistorico("Optativas Cursadas (${aprovadasOptativas.size})", aprovadasOptativas)

    tableHistorico("Complementares Cursadas (${aprovadasComplementares.size})", aprovadasComplementares)

    tableHistorico("Eletivas Cursadas (${aprovadasEletivas.size})", aprovadasEletivas)
}

/**
 * Apresenta o card com os dados do [aluno]:
 * - Matricula
 * - Nome
 * - E-mail
 */
private fun FlowContent.cardDadosAluno(aluno: Aluno) {
//    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
//
//    }
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        div(classes = "card-body") {
            h2(classes = "card-title") { +"${aluno.matricula} - ${aluno.nome}" }
            p { +"Email: ${aluno.email}" }
        }
    }
}

/**
 * Apresenta o card com os semestres que o [aluno] tem para cursar, desde o semestre inicial até
 * o último semestre possível. Para cada semestre apresenta informações conforme o [historico]:
 * - T para o semestre trancado
 * - An, Rn ou Mn para as n disciplinas aprovadas, reprovadas ou matriculadas, respectivamente
 * Além disso, os semestres que contam para a integralização são numerados como 1, 2, 3, ...
 */
private fun FlowContent.cardSemestres(
    aluno: Aluno,
    historico: List<ItemHistorico>
) {
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "table") {
            thead {
                tr(classes = "bg-base-300") {
                    for (s in aluno.semestreInicial..aluno.semestreFinal) {
                        th(classes = "text-center pl-2 pr-2") { +s.toString() }
                    }
                }
            }
            tbody {
                val numeracao: MutableList<String> = mutableListOf()
                val background: MutableList<String> = mutableListOf()
                tr {
                    var i = mutableListOf(0)
                    val periodoPandemia = INICIO_PANDEMIA..FIM_PANDEMIA
                    for (s in aluno.semestreInicial..aluno.semestreFinal) {
                        val isPandemia = s in periodoPandemia
                        val label = situacaoSemestre(s, historico.filter { it.ano == s.ano && it.periodo == s.semestre })
                        numeracao.add(numeracaoSemestre(isPandemia, label, i))
                        background.add(estiloSemestre(isPandemia, label))
                        td(classes = "text-base text-center ${background.last()}") { +label }
                    }
                }
                tr {
                    numeracao.forEachIndexed { i, label ->
                        td(classes = "text-center ${background[i]}") { +label }
                    }
                }
            }
        }
    }
}

private fun FlowContent.tableDisciplinas(title: String, disciplinas: List<Disciplina>) {
    if (disciplinas.isEmpty()) return
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +title }
        hr(classes = "border-base-content/50") {  }
        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center") { +"Código" }
                    th { +"Nome" }
                    th(classes = "text-center") { +"Período" }
                    th(classes = "text-center") { +"Horas" }
                }
            }
            tbody {
                disciplinas
                    .forEach {
                        tr {
                            td(classes = "text-center") {
                                +it.codigo
                            }
                            td {
                                +it.nome
                            }
                            td(classes = "text-center") {
                                +"${it.periodo}"
                            }
                            td(classes = "text-center") {
                                +"${it.horas}"
                            }
                        }
                    }
            }
        }
    }
}

private fun FlowContent.tableHistorico(title: String, historico: List<ItemHistorico>) {
    if (historico.isEmpty()) return
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +title }
        hr(classes = "border-base-content/50") {  }
        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center") { +"Semestre" }
                    th(classes = "text-center") { +"Código" }
                    th { +"Nome" }
                    th(classes = "text-center") { +"Situação" }
                    th(classes = "text-right") { +"Nota" }
                    th(classes = "text-center") { +"Horas" }
                }
            }
            tbody {
                historico
                    .forEach {
                        tr {
                            td(classes = "text-center") {
                                +"${it.ano}.${it.periodo}"
                            }
                            td(classes = "text-center") {
                                +it.codigo
                            }
                            td {
                                +it.nome
                            }
                            td(classes = "text-center") {
                                +it.descricao.take(3)
                            }
                            td(classes = "text-right") {
                                +"${it.nota?.format(2) ?: ""}"
                            }
                            td(classes = "text-center") {
                                +"${it.horas}"
                            }
                        }
                    }
            }
        }
    }
}





private fun estiloSemestre(isPandemia: Boolean, label: String): String =
    when {
        isPandemia -> "bg-warning"
        label == "T" -> "bg-error"
        label == "-" -> "bg-info"
        else -> "bg-success"
    }

private fun numeracaoSemestre(isPandemia: Boolean, label: String, i: MutableList<Int>): String =
    when {
        isPandemia -> "P"
        label == "T" -> "-"
        else -> { i[0] = i[0] + 1; i[0].toString() }
    }

private fun situacaoSemestre(semestre: Semestre, historico: List<ItemHistorico>): String {
    if (historico.isEmpty()) return if (semestre > Semestre.ATUAL) "-" else "⚠"
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