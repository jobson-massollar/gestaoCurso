package adapter.input.ui

import adapter.input.rest.DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE
import adapter.input.rest.DOWNLOAD_INSCRICOES_ROUTE
import adapter.input.rest.INSCRICAO_SORTING_ACEITOS_ASC
import adapter.input.rest.INSCRICAO_SORTING_ACEITOS_DESC
import adapter.input.rest.INSCRICAO_SORTING_DISCIPLINA_ASC
import adapter.input.rest.INSCRICAO_SORTING_DISCIPLINA_DESC
import adapter.input.rest.INSCRICAO_SORTING_VAGAS_ASC
import adapter.input.rest.INSCRICAO_SORTING_VAGAS_DESC
import adapter.input.rest.INSCRICOES_ROUTE
import kotlinx.html.*
import model.Aluno
import model.Grade
import model.TotalizacaoInscricao

const val INSCRICOES_FORM = "inscricoes"

fun FlowContent.tableInscricoes(totalizacoes: List<TotalizacaoInscricao>, currentSorting: String) {
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
                                sortingButtons(INSCRICOES_ROUTE, INSCRICOES_FORM, INSCRICAO_SORTING_DISCIPLINA_ASC,
                                    INSCRICAO_SORTING_DISCIPLINA_DESC
                                )
                            }
                            th(classes = "text-right") {
                                +"Aceitas"
                                sortingButtons(INSCRICOES_ROUTE, INSCRICOES_FORM, INSCRICAO_SORTING_ACEITOS_ASC,
                                    INSCRICAO_SORTING_ACEITOS_DESC
                                )
                            }
                            th(classes = "text-right") {
                                +"Falta de Vagas"
                                sortingButtons(INSCRICOES_ROUTE, INSCRICOES_FORM, INSCRICAO_SORTING_VAGAS_ASC,
                                    INSCRICAO_SORTING_VAGAS_DESC
                                )
                            }
                            th(classes = "text-right") { +"Falta de Pré-req" }
                            th(classes = "text-right") { +"Canceladas" }
                            th { +" " }
                        }
                    }
                    tbody {
                        totalizacoes.forEach {
                            tr {
                                td(classes = "text-center") { +it.turma }
                                td(classes = "text-center") { +it.codigo }
                                td { +it.nome }
                                td(classes = "text-right") { +it.aceitos.toString() }
                                td(classes = "text-right") { +it.faltaVagas.toString() }
                                td(classes = "text-right") { +it.faltaPreRequisito.toString() }
                                td(classes = "text-right") { +it.cancelados.toString() }
                                td { +" " }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.tableInscricoesIrregulares(alunos: List<Aluno>) {
    title("Inscrições Irregulares", DOWNLOAD_INSCRICOES_IRREGULARES_ROUTE) {

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
                        th(classes = "text-center") { +"Inscrições" }
                        th { +"Pode Cursar" }
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
                                    $"/alunos/painel/${aluno.matricula}",
                                    "#main-container",
                                    !aluno.estaAtivo
                                )
                                smallButton(
                                    "Histórico",
                                    $"/historico/${aluno.matricula}",
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

        if (disciplinasObrigatoriasACursar.isNotEmpty())
            observacoes.add("${disciplinasObrigatoriasACursar.size} obrigatórias")

        if (horasOptativasFaltantes > 0)
            observacoes.add("${horasOptativasFaltantes}h de optativas")

        if (grade is Grade.Grade2008 && horasEletivasFaltantes > 0)
            observacoes.add("${horasEletivasFaltantes}h de eletivas")

        return observacoes
    }