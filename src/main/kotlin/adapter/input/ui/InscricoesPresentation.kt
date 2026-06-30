package adapter.input.ui

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import kotlinx.html.ul
import model.Aluno
import model.Grade

fun FlowContent.tableInscricoes(alunos: List<Aluno>) {
    title("Inscrições Irregulares") {
        if (alunos.isEmpty()) {
            p(classes = "text-base") { +"Nenhuma inscrição irregular foi encontrada!"}
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
                        th { +"Observações" }
                        th { +" " }
                    }
                }
                alunos.forEach {
                    tr(classes = "hover:bg-accent hover:text-base-100") {
                        td(classes = "text-center") { +it.matricula }
                        td { +it.nome }
                        td(classes = "text-center") { +it.versao }
                        td { +it.email }
                        td(classes = "text-center") { +it.itensMatriculados.size.toString() }
                        td {
                            ul {
                                if (it.disciplinasObrigatoriasACursar.isNotEmpty()) {
                                    li { +"${it.disciplinasObrigatoriasACursar.size} obrigatórias" }
                                }
                                if (it.horasOptativasFaltantes > 0) {
                                    li { +"${it.horasOptativasFaltantes}h de optativas" }
                                }
                                if (it.grade is Grade.Grade2008) {
                                    if (it.horasEletivasFaltantes > 0) {
                                        li { +"${it.horasEletivasFaltantes}h de eletivas" }
                                    }
                                }
                            }
                        }
                        td(classes = "gap-4") {
                            smallButton("Painel", $"/alunos/painel/${it.matricula}", "#main-container", !it.isAtivo)
                            smallButton("Histórico", $"/historico/${it.matricula}", "#main-container", !it.isAtivo)
                        }
                    }
                }
            }
        }
    }
}