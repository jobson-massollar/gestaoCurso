package adapter.input.rest

import services.application.AlunoFilter
import services.application.AlunoSorting

const val ALUNO_SORTING_MATRICULA_ASC = "matricula"
const val ALUNO_SORTING_MATRICULA_DESC = "-matricula"
const val ALUNO_SORTING_NOME_ASC = "nome"
const val ALUNO_SORTING_NOME_DESC = "-nome"

val ALUNO_FILTER_ALL = AlunoFilter.ALL.name.lowercase()
val ALUNO_FILTER_ACTIVE = AlunoFilter.ACTIVE.name.lowercase()

fun getAlunoSortingByValue(value: String) =
    when (value) {
        ALUNO_SORTING_MATRICULA_ASC -> AlunoSorting.MATRICULA_ASCENDING
        ALUNO_SORTING_MATRICULA_DESC -> AlunoSorting.MATRICULA_DESCENDING
        ALUNO_SORTING_NOME_DESC -> AlunoSorting.NOME_DESCENDING
        else -> AlunoSorting.NOME_ASCENDING
    }

fun getValueByAlunoSorting(sorting: AlunoSorting) =
    when (sorting) {
        AlunoSorting.MATRICULA_ASCENDING -> "matricula"
        AlunoSorting.MATRICULA_DESCENDING -> "-matricula"
        AlunoSorting.NOME_ASCENDING -> "nome"
        AlunoSorting.NOME_DESCENDING -> "-nome"
    }

fun getAlunoFilterByValue(value: String) =
    runCatching {
        AlunoFilter.valueOf(value.uppercase())
    }.getOrDefault(AlunoFilter.ALL)

fun getValueByAlunoFilter(filter: AlunoFilter) = filter.name.lowercase()