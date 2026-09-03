package adapter.input.ui

import adapter.input.rest.*
import kotlinx.html.*
import main.dateFormat
import main.timeFormat
import model.*

const val INSCRICOES_FORM = "inscricoes"

fun FlowContent.tableTotalizacaoInscricoes(totalizacoes: List<TotalizacaoInscricao>, currentSorting: String) {
    title("Inscrições", DOWNLOAD_INSCRICOES_ROUTE) {

        if (totalizacoes.isEmpty()) {
            p(classes = "text-base") { +"Nenhuma inscrição foi encontrada!" }
            return@title
        }

        form {
            name = INSCRICOES_FORM
            hiddenInput {
                name = "sort"
                value = currentSorting
            }

            div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
                table(classes = "table table-zebra table-sm") {
                    thead {
                        tr(classes = "bg-base-300") {
                            th(classes = "text-center") { +"Turma" }
                            th(classes = "text-center") { +"Código" }
                            th {
                                +"Nome"
                                sortingButtons(
                                    INSCRICOES_ROUTE,
                                    INSCRICOES_FORM,
                                    INSCRICAO_SORTING_DISCIPLINA_ASC,
                                    INSCRICAO_SORTING_DISCIPLINA_DESC
                                )
                            }
                            th(classes = "text-center") {
                                +"Solicitadas"
                                sortingButtons(
                                    INSCRICOES_ROUTE,
                                    INSCRICOES_FORM,
                                    INSCRICAO_SORTING_SOLICITADOS_ASC,
                                    INSCRICAO_SORTING_SOLICITADOS_DESC
                                )
                            }
                            th {
                                +"Aceitas"
                                sortingButtons(
                                    INSCRICOES_ROUTE,
                                    INSCRICOES_FORM,
                                    INSCRICAO_SORTING_ACEITOS_ASC,
                                    INSCRICAO_SORTING_ACEITOS_DESC
                                )
                            }
                            th {
                                +"Falta de Vagas"
                                sortingButtons(
                                    INSCRICOES_ROUTE,
                                    INSCRICOES_FORM,
                                    INSCRICAO_SORTING_VAGAS_ASC,
                                    INSCRICAO_SORTING_VAGAS_DESC
                                )
                            }
                            th { +"Falta de Pré-req" }
                            th { +"Canceladas" }
                            th { +" " }
                        }
                    }
                    tbody {
                        totalizacoes.forEach {disciplina ->
                            tr {
                                td(classes = "text-center") { +disciplina.turma }
                                td(classes = "text-center") { +disciplina.codigo }
                                td { +disciplina.nome }
                                td(classes = "text-center") { +disciplina.solicitados.toString() }
                                td(classes = "text-center") { +disciplina.aceitos.toString() }
                                td(classes = "text-center") { +disciplina.faltaVagas.toString() }
                                td(classes = "text-center") { +disciplina.faltaPreRequisito.toString() }
                                td(classes = "text-center") { +disciplina.cancelados.toString() }
                                td {
                                    smallButton("Alunos",
                                        "$INSCRICOES_ROUTE/${disciplina.codigo}/${disciplina.turma}",
                                        "#main-container")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.tableInscricoesDisciplinaBSI(inscricoes: List<Inscricao>, turma: Turma, disciplina: Disciplina, currentSorting: String) {
    title("Inscrições - ${disciplina.codigo} - ${disciplina.nome} (${turma.codigo})", backButton = true) {
        tableInscricoes(inscricoes, turma.codigo, disciplina.codigo, currentSorting)
    }
}

fun FlowContent.tableInscricoesDisciplina(inscricoes: List<Inscricao>, codigoTurma: String, codigoDisciplina: String, currentSorting: String) {
    title("Inscrições - $codigoDisciplina - ${inscricoes[0].nome} (${codigoTurma})", backButton = true) {
        tableInscricoes(inscricoes, codigoTurma, codigoDisciplina, currentSorting)
    }
}

private fun FlowContent.tableInscricoes(inscricoes: List<Inscricao>, codigoTurma: String, codigoDisciplina: String, currentSorting: String) {
    if (inscricoes.isEmpty()) {
        p(classes = "text-base") { +"Nenhuma inscrição foi encontrada!" }
        return
    }

    form {
        name = INSCRICOES_FORM
        hiddenInput {
            name = "sort"
            value = currentSorting
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th(classes = "text-center") { +"#" }
                        th(classes = "text-center") { +"Matrícula" }
                        th {
                            +"Nome"
                            sortingButtons(
                                "$INSCRICOES_ROUTE/$codigoDisciplina/$codigoTurma",
                                INSCRICOES_FORM,
                                INSCRICAO_SORTING_NOME_ASC,
                                INSCRICAO_SORTING_NOME_DESC
                            )
                        }
                        th(classes = "text-center") { +"Currículo" }
                        th { +"E-mail" }
                        th {
                            +"Prioridade"
                            sortingButtons(
                                "$INSCRICOES_ROUTE/$codigoDisciplina/$codigoTurma",
                                INSCRICOES_FORM,
                                INSCRICAO_SORTING_PRIORIDADE_ASC,
                                INSCRICAO_SORTING_PRIORIDADE_DESC
                            )
                        }
                        th { +"Situação" }
                        th(classes = "text-center") { +"Solicitação" }
                        th(classes = "text-center") { +"Processamento" }
                        th { +" " }
                    }
                }
                tbody {
                    inscricoes.forEachIndexed { i, inscricao ->
                        tr {
                            td(classes = "text-center") { +(i + 1).toString() }
                            td(classes = "text-center") { +inscricao.matricula }
                            td { +inscricao.nomeAluno }
                            td(classes = "text-center") { +(inscricao.aluno?.let { "${it.versao}" } ?: "-") }
                            td { +(inscricao.aluno?.let { "${it.email}" } ?: "-") }
                            td(classes = "text-center") { +inscricao.prioridade.toString() }
                            td { +inscricao.descricao }
                            td(classes = "text-center") {
                                +"${dateFormat.format(inscricao.dataSolicitacao)} ${
                                    timeFormat.format(
                                        inscricao.horaSolicitacao
                                    )
                                }"
                            }
                            td(classes = "text-center") {
                                +(inscricao.dataProcessamento?.let { dateFormat.format(it) } ?: "")
                            }
                            td(classes = "gap-4") {
                                smallButton(
                                    "Painel",
                                    "$PAINEL_ALUNO_ROUTE/${inscricao.matricula}",
                                    "#main-container",
                                    !inscricao.matricula.ehAlunoBSI
                                )
                                smallButton(
                                    "Histórico",
                                    "$HISTORICO_ROUTE/${inscricao.matricula}",
                                    "#main-container",
                                    !inscricao.matricula.ehAlunoBSI
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.tableInscricoesAluno(aluno: Aluno) {
    title("Inscrições - ${aluno.matricula} - ${aluno.nome} (${aluno.versao})", backButton = true) {

        if (aluno.inscricoes.isEmpty()) {
            p(classes = "text-base") { +"Nenhuma inscrição foi encontrada!" }
            return@title
        }

        div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
            table(classes = "table table-zebra table-sm") {
                thead {
                    tr(classes = "bg-base-300") {
                        th(classes = "text-center") { +"Turma" }
                        th(classes = "text-center") { +"Código" }
                        th { +"Disciplina" }
                        th { +"Situação" }
                        th(classes = "text-center") { +"Solicitação" }
                        th(classes = "text-center") { +"Processamento" }
                    }
                }
                tbody {
                    aluno.inscricoes.forEach { inscricao ->
                        tr {
                            td(classes = "text-center") { +inscricao.turma }
                            td(classes = "text-center") { +inscricao.codigo }
                            td { +inscricao.nome }
                            td { +inscricao.descricao }
                            td(classes = "text-center") { +"${dateFormat.format(inscricao.dataSolicitacao)} ${timeFormat.format(inscricao.horaSolicitacao)}" }
                            td(classes = "text-center") { +(inscricao.dataProcessamento?.let { dateFormat.format(it) } ?: "") }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.tableInscricoesIrregulares(alunos: List<Aluno>) {
    title("Alunos com menos de 3 Inscrições", DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE) {

        if (alunos.isEmpty()) {
            p(classes = "text-base") { +"Nenhuma inscrição irregular foi encontrada!" }
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
                        th(classes = "text-center") { +"Matriculado" }
                        th(classes = "text-center") { +"Trancamentos" }
                        th { +"Pode Matricular" }
                        th { +" " }
                    }
                }
                tbody {
                    alunos.forEach { aluno ->
                        tr(classes = "hover:bg-accent hover:text-base-100") {
                            td(classes = "text-center") { +aluno.matricula }
                            td { +aluno.nome }
                            td(classes = "text-center") { +aluno.versao }
                            td { +aluno.email }
                            td(classes = "text-center") { +aluno.itensMatriculados.size.toString() }
                            td(classes = "text-center") {
                                ul {
                                    aluno.historico.trancados.forEach { periodo ->
                                        li { +periodo.toString() }
                                    }
                                }
                            }
                            td {
                                ul {
                                    aluno.observacoes.forEach { obs ->
                                        li { +obs }
                                    }
                                }
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
                                smallButton(
                                    "Inscrições",
                                    "$INSCRICOES_ROUTE/${aluno.matricula}",
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

val Aluno.observacoes: List<String>
    get() {
        val observacoes = mutableListOf<String>()

        if (disciplinasObrigatoriasPodeMatricular.isNotEmpty())
            observacoes.add("${disciplinasObrigatoriasPodeMatricular.size} obrigatórias")

        if (horasOptativasFaltantes > 0)
            observacoes.add("${horasOptativasFaltantes}h de optativas")

        if (grade is Grade.Grade2008 && horasEletivasFaltantes > 0)
            observacoes.add("${horasEletivasFaltantes}h de eletivas")

        return observacoes
    }