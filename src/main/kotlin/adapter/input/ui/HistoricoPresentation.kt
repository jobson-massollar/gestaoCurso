package adapter.input.ui.template

import kotlinx.html.*
import model.Aluno
import model.Semestre

fun FlowContent.historicoAluno(aluno: Aluno) {
    val inicio = Semestre(aluno.matricula.take(4).toInt(), aluno.matricula[4].code - 48)
    val fim = inicio + (if (inicio < Semestre(2020, 1)) 23 else 17)

    div(classes = "shadow-sm overflow-x-auto rounded-box border border-base-content/50 bg-base-100") {
        table(classes = "border m-4 text-sm") {
            thead {
                tr(classes = "bg-base-300") {
                    for (s in inicio..fim) {
                        th(classes = "border text-center pl-2 pr-2") { +s.toString() }
                    }
                }
            }
            tr {
                (inicio..fim).forEach {
                    td(classes = "border text-center") { +"todo" }
                }
            }
        }
    }
}