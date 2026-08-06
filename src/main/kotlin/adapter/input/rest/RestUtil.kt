package adapter.input.rest

import services.application.AlunoFilter
import services.application.AlunoSorting
import services.application.InscricaoSorting

const val ALUNO_SORTING_MATRICULA_ASC = "matricula"
const val ALUNO_SORTING_MATRICULA_DESC = "-matricula"
const val ALUNO_SORTING_NOME_ASC = "nome"
const val ALUNO_SORTING_NOME_DESC = "-nome"

fun getAlunoSortingByValue(value: String) =
    when (value) {
        ALUNO_SORTING_MATRICULA_ASC -> AlunoSorting.MATRICULA_ASCENDING
        ALUNO_SORTING_MATRICULA_DESC -> AlunoSorting.MATRICULA_DESCENDING
        ALUNO_SORTING_NOME_DESC -> AlunoSorting.NOME_DESCENDING
        else -> AlunoSorting.NOME_ASCENDING
    }

val ALUNO_FILTER_ALL = AlunoFilter.ALL.name.lowercase()
val ALUNO_FILTER_ACTIVE = AlunoFilter.ACTIVE.name.lowercase()
val ALUNO_FILTER_GRADUATED = AlunoFilter.GRADUATED.name.lowercase()
val ALUNO_FILTER_GRADUATING = AlunoFilter.GRADUATING.name.lowercase()

fun getAlunoFilterByValue(value: String) =
    runCatching {
        AlunoFilter.valueOf(value.uppercase())
    }.getOrDefault(AlunoFilter.ALL)

const val INSCRICAO_SORTING_DISCIPLINA_ASC = "disciplina"
const val INSCRICAO_SORTING_DISCIPLINA_DESC = "-disciplina"
const val INSCRICAO_SORTING_SOLICITADOS_ASC = "solicitados"
const val INSCRICAO_SORTING_SOLICITADOS_DESC = "-solicitados"
const val INSCRICAO_SORTING_ACEITOS_ASC = "aceitos"
const val INSCRICAO_SORTING_ACEITOS_DESC = "-aceitos"
const val INSCRICAO_SORTING_VAGAS_ASC = "vagas"
const val INSCRICAO_SORTING_VAGAS_DESC = "-vagas"

fun getInscricaoSortingByValue(value: String) =
    when (value) {
        INSCRICAO_SORTING_DISCIPLINA_ASC -> InscricaoSorting.DISCIPLINA_ASCENDING
        INSCRICAO_SORTING_DISCIPLINA_DESC -> InscricaoSorting.DISCIPLINA_DESCENDING
        INSCRICAO_SORTING_SOLICITADOS_ASC -> InscricaoSorting.SOLICITADOS_ASCENDING
        INSCRICAO_SORTING_SOLICITADOS_DESC -> InscricaoSorting.SOLICITADOS_DESCENDING
        INSCRICAO_SORTING_ACEITOS_ASC -> InscricaoSorting.ACEITOS_ASCENDING
        INSCRICAO_SORTING_ACEITOS_DESC -> InscricaoSorting.ACEITOS_DESCENDING
        INSCRICAO_SORTING_VAGAS_ASC -> InscricaoSorting.VAGAS_ASCENDING
        INSCRICAO_SORTING_VAGAS_DESC -> InscricaoSorting.VAGAS_DESCENDING
        else -> InscricaoSorting.DISCIPLINA_ASCENDING
    }

//fun getValueByAlunoSorting(sorting: AlunoSorting) =
//    when (sorting) {
//        AlunoSorting.MATRICULA_ASCENDING -> "matricula"
//        AlunoSorting.MATRICULA_DESCENDING -> "-matricula"
//        AlunoSorting.NOME_ASCENDING -> "nome"
//        AlunoSorting.NOME_DESCENDING -> "-nome"
//    }

//fun getValueByAlunoFilter(filter: AlunoFilter) = filter.name.lowercase()