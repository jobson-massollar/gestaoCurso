package adapter.input.ui

import kotlinx.html.*
import model.Aluno
import model.COMPLEMENTAR
import model.Disciplina
import model.ELETIVA
import model.FIM_PANDEMIA
import model.INICIO_PANDEMIA
import model.ItemHistorico
import model.OBRIGATORIA
import model.OPTATIVA
import model.Periodo
import model.aprovadas
import model.complementares
import model.cursadas
import model.eletivas
import model.matriculadas
import model.obrigatorias
import model.optativas
import model.reprovadas

fun FlowContent.historicoAluno(aluno: Aluno,
                               historico: List<ItemHistorico>,
                               aprovadas: List<ItemHistorico>,
                               matriculadas: List<ItemHistorico>,
                               obrigatoriasFaltantes: List<Disciplina>) {

    val aprovadasObrigatorias = aprovadas.obrigatorias
    val aprovadasOptativas = aprovadas.optativas
    val aprovadasComplementares = aprovadas.complementares
    val aprovadasEletivas = aprovadas.eletivas

    cardDadosAluno(aluno, aprovadasObrigatorias, aprovadasOptativas, aprovadasComplementares, aprovadasEletivas)

    cardPeriodos(aluno, historico)

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
private fun FlowContent.cardDadosAluno(
    aluno: Aluno,
    aprovadasObrigatorias: List<ItemHistorico>,
    aprovadasOptativas: List<ItemHistorico>,
    aprovadasComplementares: List<ItemHistorico>,
    aprovadasEletivas: List<ItemHistorico>
) {
//    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
//
//    }
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        div(classes = "card-body") {
            h2(classes = "card-title") { +"${aluno.matricula} - ${aluno.nome} (${aluno.versao})" }
            p { +"✉: ${aluno.email}" }
            hr(classes = "border-base-content/50") {  }
            p { +"Obrigatórias: ${aprovadasObrigatorias.size} / ${aprovadasObrigatorias.sumOf { it.horas }}h" }
            p { +"Optativas: ${aprovadasOptativas.size} / ${aprovadasOptativas.sumOf { it.horas }}h" }
            p { +"Complementares: ${aprovadasComplementares.size} / ${aprovadasComplementares.sumOf { it.horas }}h" }
            p { +"Eletivas: ${aprovadasEletivas.size} / ${aprovadasEletivas.sumOf { it.horas }}h" }
            hr(classes = "border-base-content/50") {  }
            p { +"Trancamentos: ${aluno.trancamentos}"}
            p { +"Prazo de extensão: ${aluno.prazoExtensao} período(s)"}
            p { +"Período limite: ${aluno.periodoLimite.ano}.${aluno.periodoLimite.semestre}"}
        }
    }
}

/**
 * Apresenta o card com os períodos que o [aluno] tem para cursar, desde o período inicial até
 * o último período possível. Para cada semestre apresenta informações conforme o [historico]:
 * - T para o período trancado
 * - An, Rn ou Mn para as n disciplinas aprovadas, reprovadas ou matriculadas, respectivamente
 *
 * Além disso, os períodos que contam para a integralização são numerados como 1, 2, 3, ...
 */
private fun FlowContent.cardPeriodos(
    aluno: Aluno,
    historico: List<ItemHistorico>
) {
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "table") {
            thead {
                tr(classes = "bg-base-300") {
                    for (s in aluno.periodoInicial..aluno.periodoFinal) {
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
                    for (s in aluno.periodoInicial..aluno.periodoFinal) {
                        val isPandemia = s in periodoPandemia
                        val label = situacaoPeriodo(s, historico.cursadas(s))
                        numeracao.add(numeracaoPeriodo(isPandemia, label, i))
                        background.add(estiloPeriodo(isPandemia, s > aluno.periodoLimite, label))
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

private fun estiloPeriodo(isPandemia: Boolean, isAcimaLImite: Boolean, label: String): String =
    when {
        isPandemia -> "bg-warning"
        label == "T" -> "bg-error"
        isAcimaLImite -> ""
        label == "-" -> "bg-info"
        else -> "bg-success"
    }

private fun numeracaoPeriodo(isPandemia: Boolean, label: String, i: MutableList<Int>): String =
    when {
        isPandemia -> "P"
        label == "T" -> "-"
        else -> { i[0] = i[0] + 1; i[0].toString() }
    }

private fun situacaoPeriodo(periodo: Periodo, historico: List<ItemHistorico>): String {
    if (historico.isEmpty()) return if (periodo > Periodo.ATUAL) "-" else "⚠"
    if (historico[0].isTrancamento) return "T"

    val m = historico.matriculadas.size
    val a = historico.aprovadas.size
    val r = historico.reprovadas.size

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
        if (r > 0)
            append("${s}${r}R")
    }
}