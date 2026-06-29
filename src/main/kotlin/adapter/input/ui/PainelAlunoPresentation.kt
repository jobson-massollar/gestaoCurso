package adapter.input.ui

import kotlinx.html.*
import model.*

fun FlowContent.painelAluno(aluno: Aluno) {

    val aprovadasObrigatorias = aluno.itensAprovados.obrigatorias
    val aprovadasOptativas = aluno.itensAprovados.optativas
    val aprovadasComplementares = aluno.itensAprovados.complementares
    val aprovadasEletivas = aluno.itensAprovados.eletivas
    val obrigatoriasFaltantes = aluno.disciplinasObrigatoriasFaltantes

    cardDadosAluno(aluno, aprovadasObrigatorias, aprovadasOptativas, aprovadasComplementares, aprovadasEletivas)

    cardPeriodos(aluno)

    tableObrigatoriasFaltantes("Obrigatórias que Faltam (${obrigatoriasFaltantes.size})", obrigatoriasFaltantes, aluno.itensMatriculados)

    tableDisciplinas("Matriculadas (${aluno.itensMatriculados.size})", aluno.itensMatriculados)

    tableDisciplinas("Obrigatórias Cursadas (${aprovadasObrigatorias.size})", aprovadasObrigatorias)

    tableDisciplinas("Optativas Cursadas (${aprovadasOptativas.size})", aprovadasOptativas)

    tableDisciplinas("Complementares Cursadas (${aprovadasComplementares.size})", aprovadasComplementares)

    tableDisciplinas("Eletivas Cursadas (${aprovadasEletivas.size})", aprovadasEletivas)
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
            if (aluno.ehFormando || aluno.estaFormado) {
                hr(classes = "border-base-content/50") { }
                div(classes="alert bg-accent") {
                    role = "alert"
                    p(classes="text-base font-bold") { +if (aluno.estaFormado) "\uD83D\uDE00 Formado!" else "\uD83D\uDE42 Formando!"}
                }
            }
        }
    }
}

/**
 * Apresenta o card com os períodos que o [aluno] tem para cursar, desde o período inicial até
 * o último período possível. Para cada semestre apresenta informações:
 * - T para o período trancado
 * - An, Rn ou Mn para as n disciplinas aprovadas, reprovadas ou matriculadas, respectivamente
 *
 * Além disso, os períodos que contam para a integralização são numerados como 1, 2, 3, ...
 */
private fun FlowContent.cardPeriodos(aluno: Aluno) {
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
                    val i = mutableListOf(0)
                    val periodoPandemia = INICIO_PANDEMIA..FIM_PANDEMIA
                    for (s in aluno.periodoInicial..aluno.periodoFinal) {
                        val isPandemia = s in periodoPandemia
                        val label = situacaoPeriodo(s, aluno)
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

private fun FlowContent.tableObrigatoriasFaltantes(title: String, disciplinas: List<Disciplina>, matriculadas: List<ItemHistorico>) {
    if (disciplinas.isEmpty()) return
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        h2(classes = "m-2 text-base-content/50 font-bold") { +title }
        hr(classes = "border-base-content/50") {  }
        table(classes = "table table-zebra table-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center") { +"Código" }
                    th { +"Nome" }
                    th(classes = "text-center") { +"Matriculada" }
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
                                +if (matriculadas.any { m -> m.codigo == it.codigo }) "✅" else "-"
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

private fun FlowContent.tableDisciplinas(title: String, historico: List<ItemHistorico>) {
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
                                +(it.nota?.format(2) ?: "")
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

private fun situacaoPeriodo(periodo: Periodo, aluno: Aluno): String {
    val historicoPeriodo = aluno.historico.cursadas(periodo)

    if (historicoPeriodo.isEmpty()) return if (periodo > Periodo.ATUAL) "-" else "⚠"
    if (historicoPeriodo[0].isTrancamento) return "T"

    val m = historicoPeriodo.matriculadas.size
    val a = historicoPeriodo.aprovadas.size
    val r = historicoPeriodo.reprovadas.size

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