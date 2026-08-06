package adapter.input.ui

import adapter.input.rest.HISTORICO_ROUTE
import adapter.input.rest.INSCRICOES_ROUTE
import adapter.input.rest.PAINEL_ALUNO_ROUTE
import kotlinx.html.*
import main.collator
import model.*

fun FlowContent.painelAluno(aluno: Aluno) {
    val comparator: Comparator<ItemHistorico> = compareBy(collator) { it.nome }
    val aprovadasObrigatorias = aluno.itensAprovados.obrigatorias.sortedWith(comparator)
    val aprovadasOptativas = aluno.itensAprovados.optativas.sortedWith(comparator)
    val aprovadasComplementares = aluno.itensAprovados.complementares.sortedWith(comparator)
    val aprovadasEletivas = aluno.itensAprovados.eletivas.sortedWith(comparator)
    val obrigatoriasFaltantes = aluno.disciplinasObrigatoriasFaltantes.sortedWith(compareBy(collator) { it.nome })
    val matriculadas = aluno.itensMatriculados.sortedWith(comparator)

    title("Painel do Aluno", backButton = true) {
        cardDadosAluno(aluno, aprovadasObrigatorias, aprovadasOptativas, aprovadasComplementares, aprovadasEletivas)

        cardPeriodos(aluno)

        tableObrigatoriasFaltantes(
            "Obrigatórias que Faltam (${obrigatoriasFaltantes.size})",
            obrigatoriasFaltantes,
            aluno.itensMatriculados
        )

        tableDisciplinas("Matriculadas (${matriculadas.size})", matriculadas)

        tableDisciplinas("Obrigatórias Cursadas (${aprovadasObrigatorias.size})", aprovadasObrigatorias)

        tableDisciplinas("Optativas Cursadas (${aprovadasOptativas.size})", aprovadasOptativas)

        tableDisciplinas("Complementares Cursadas (${aprovadasComplementares.size})", aprovadasComplementares)

        tableDisciplinas("Eletivas Cursadas (${aprovadasEletivas.size})", aprovadasEletivas)
    }
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
    div(classes = "mb-4 shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        div(classes = "card-body") {
            h2(classes = "card-title") { +"${aluno.matricula} - ${aluno.nome} (${aluno.versao})" }
            p { +"✉: ${aluno.email}" }
            if (aluno.ehFormando || aluno.estaFormado) {
                //hr(classes = "border-base-content/50") { }
                div(classes="alert bg-accent") {
                    role = "alert"
                    p(classes="text-base font-bold") { +if (aluno.estaFormado) "\uD83D\uDE00 Formado!" else "\uD83D\uDE42 Formando!"}
                }
            }
            hr(classes = "border-base-content/50") {  }
//            p { +"Obrigatórias: ${aprovadasObrigatorias.size} / ${aprovadasObrigatorias.sumOf { it.horas }}h" }
//            p { +"Optativas: ${aprovadasOptativas.size} / ${aprovadasOptativas.sumOf { it.horas }}h" }
//            p { +"Complementares: ${aprovadasComplementares.size} / ${aprovadasComplementares.sumOf { it.horas }}h" }
//            p { +"Eletivas: ${aprovadasEletivas.size} / ${aprovadasEletivas.sumOf { it.horas }}h" }
//            hr(classes = "border-base-content/50") {  }
            div(classes="flex") {
                div(classes="my-1 leading-7") {
                    p { +"Obrigatórias: ${aprovadasObrigatorias.size} / ${aprovadasObrigatorias.sumOf { it.horas }}h" }
                    p { +"Optativas: ${aprovadasOptativas.size} / ${aprovadasOptativas.sumOf { it.horas }}h" }
                    p { +"Complementares: ${aprovadasComplementares.size} / ${aprovadasComplementares.sumOf { it.horas }}h" }
                    p { +"Eletivas: ${aprovadasEletivas.size} / ${aprovadasEletivas.sumOf { it.horas }}h" }
                }
                div(classes="mx-30 my-1 leading-7") {
                    p { +"Trancamentos: ${aluno.trancamentos}"}
                    p { +"Prazo de extensão: ${aluno.prazoExtensao} período(s)"}
                    p { +"Período limite: ${aluno.periodoLimite.ano}.${aluno.periodoLimite.semestre}"}
                }
            }
            hr(classes = "border-base-content/50") {  }
            span(classes = "gap-4") {
                smallButton(
                    "Histórico",
                    "$HISTORICO_ROUTE/${aluno.matricula}",
                    "#main-container",
                    !aluno.estaAtivo
                )
                smallButton(
                    "Inscrições",
                    "$INSCRICOES_ROUTE/${aluno.matricula}",
                    "#main-container",
                    !aluno.estaAtivo
                )
            }
//            p { +"Trancamentos: ${aluno.trancamentos}"}
//            p { +"Prazo de extensão: ${aluno.prazoExtensao} período(s)"}
//            p { +"Período limite: ${aluno.periodoLimite.ano}.${aluno.periodoLimite.semestre}"}
//            if (aluno.ehFormando || aluno.estaFormado) {
//                hr(classes = "border-base-content/50") { }
//                div(classes="alert bg-accent") {
//                    role = "alert"
//                    p(classes="text-base font-bold") { +if (aluno.estaFormado) "\uD83D\uDE00 Formado!" else "\uD83D\uDE42 Formando!"}
//                }
//            }
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
                val background: MutableList<String> = mutableListOf()
                tr {
                    aluno.statusPeriodos(Periodo.ATUAL).forEach { statusPeriodo ->
                        val label = situacaoPeriodo(statusPeriodo, aluno)
                        background.add(estiloPeriodo(statusPeriodo))
                        td(classes = "text-base text-center ${background.last()}") { +label }
                    }
                }
                tr {
                    aluno.statusPeriodos(Periodo.ATUAL).forEachIndexed { i, status ->
                        td(classes = "text-center ${background[i]}") { +numeracaoPeriodo(status) }
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
                    th(classes = "text-center w-1/10") { +"Código" }
                    th(classes = "w-6/10") { +"Nome" }
                    th(classes = "text-center w-1/10") { +"Matriculada" }
                    th(classes = "text-center w-1/10") { +"Período" }
                    th(classes = "text-center w-1/10") { +"Horas" }
                }
            }
            tbody {
                disciplinas
                    .forEach { disciplina ->
                        tr {
                            td(classes = "text-center") { +disciplina.codigo  }
                            td { +disciplina.nome }
                            td(classes = "text-center") { +if (matriculadas.any { m -> m.codigo == disciplina.codigo }) "✅" else "-" }
                            td(classes = "text-center") { +"${disciplina.periodo}" }
                            td(classes = "text-center") { +"${disciplina.horas}" }
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
        table(classes = "table table-zebra table-sm table-fixed") {
            thead {
                tr(classes = "bg-base-300") {
                    th(classes = "text-center w-1/10") { +"Semestre" }
                    th(classes = "text-center w-1/10") { +"Código" }
                    th(classes = "w-5/10") { +"Nome" }
                    th(classes = "text-center w-1/10") { +"Situação" }
                    th(classes = "text-right w-1/10") { +"Nota" }
                    th(classes = "text-center w-1/10") { +"Horas" }
                }
            }
            tbody {
                historico
                    .forEach { item ->
                        tr {
                            td(classes = "text-center") {
                                +"${item.ano}.${item.periodo}"
                            }
                            td(classes = "text-center") {
                                +item.codigo
                            }
                            td {
                                +item.nome
                            }
                            td(classes = "text-center") {
                                +item.descricao.take(3)
                            }
                            td(classes = "text-right") {
                                +(item.nota?.format(2) ?: "")
                            }
                            td(classes = "text-center") {
                                +"${item.horas}"
                            }
                        }
                    }
            }
        }
    }
}

private fun estiloPeriodo(status: StatusPeriodo): String =
    when {
        status.isPandemia -> "bg-warning"
        status.isAcimaLimite -> if (status is StatusPeriodo.ACursar) "" else "bg-red-300"
        status is StatusPeriodo.Trancado -> "bg-secondary"
        status is StatusPeriodo.ACursar -> "bg-info"
        else -> "bg-success"
    }

private fun numeracaoPeriodo(status: StatusPeriodo): String =
    when {
        status.isPandemia -> "P"
        status is StatusPeriodo.Trancado -> "-"
        else -> status.numero.toString()
    }

private fun situacaoPeriodo(status: StatusPeriodo, aluno: Aluno): String =
    when (status) {
        is StatusPeriodo.ACursar -> "-"
        is StatusPeriodo.Trancado -> "🔒" // 🔒
        is StatusPeriodo.NaoMatriculado -> "❓" // "🔴⁉️"
        is StatusPeriodo.Matriculado -> {
            val historicoPeriodo = aluno.historico.cursadas(status.periodo)

            val m = historicoPeriodo.matriculadas.size
            val a = historicoPeriodo.aprovadas.size
            val r = historicoPeriodo.reprovadas.size

            buildString {
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
    }