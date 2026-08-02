package adapter.input.ui

import kotlinx.html.*
import model.Aluno
import model.Grade

fun FlowContent.tableInscricoes(alunos: List<Aluno>) {
    title("Inscrições Irregulares", "/inscricoes/irregulares/download") {

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