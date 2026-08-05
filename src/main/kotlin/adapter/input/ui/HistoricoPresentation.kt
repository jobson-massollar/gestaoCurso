package adapter.input.ui

import kotlinx.html.*
import main.collator
import model.Aluno
import model.ItemHistorico
import model.Periodo

fun FlowContent.historicoAluno(aluno: Aluno) {
    val comparator: Comparator<ItemHistorico> = compareBy(collator) { it.nome }

    title("Histórico - ${aluno.nome} (${aluno.matricula})", backButton = true) {
        aluno.historico
            .groupBy { Periodo(it.ano, it.periodo) }
            .toSortedMap()
            .forEach { (periodo, itens) ->
                tableDisciplinas("$periodo",itens.sortedWith(comparator))
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
                    th(classes = "text-center w-1/10") { +"Código" }
                    th(classes = "w-6/10") { +"Nome" }
                    th(classes = "text-center w-1/10") { +"Horas" }
                    th(classes = "text-center w-1/10") { +"Situação" }
                    th(classes = "text-right w-1/10") { +"Nota" }
                }
            }
            tbody {
                historico
                    .forEach { item ->
                        tr {
                            td(classes = "text-center") { +item.codigo }
                            td { +item.nome }
                            td(classes = "text-center") { +"${item.horas}" }
                            td(classes = "text-center") { +item.descricao.take(3) }
                            td(classes = "text-right") { +(item.nota?.format(2) ?: "") }
                        }
                    }
            }
        }
    }
}