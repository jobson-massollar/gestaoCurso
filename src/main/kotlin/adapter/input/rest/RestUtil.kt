package adapter.input.rest

import services.application.*

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

fun getTotalizacaoInscricaoSortingByValue(value: String) =
    when (value) {
        INSCRICAO_SORTING_DISCIPLINA_ASC -> TotalizacaoInscricaoSorting.DISCIPLINA_ASCENDING
        INSCRICAO_SORTING_DISCIPLINA_DESC -> TotalizacaoInscricaoSorting.DISCIPLINA_DESCENDING
        INSCRICAO_SORTING_SOLICITADOS_ASC -> TotalizacaoInscricaoSorting.SOLICITADOS_ASCENDING
        INSCRICAO_SORTING_SOLICITADOS_DESC -> TotalizacaoInscricaoSorting.SOLICITADOS_DESCENDING
        INSCRICAO_SORTING_ACEITOS_ASC -> TotalizacaoInscricaoSorting.ACEITOS_ASCENDING
        INSCRICAO_SORTING_ACEITOS_DESC -> TotalizacaoInscricaoSorting.ACEITOS_DESCENDING
        INSCRICAO_SORTING_VAGAS_ASC -> TotalizacaoInscricaoSorting.VAGAS_ASCENDING
        INSCRICAO_SORTING_VAGAS_DESC -> TotalizacaoInscricaoSorting.VAGAS_DESCENDING
        else -> TotalizacaoInscricaoSorting.DISCIPLINA_ASCENDING
    }

const val INSCRICAO_SORTING_NOME_ASC = "nome"
const val INSCRICAO_SORTING_NOME_DESC = "-nome"
const val INSCRICAO_SORTING_PRIORIDADE_ASC = "prioridade"
const val INSCRICAO_SORTING_PRIORIDADE_DESC = "-prioridade"

fun getInscricaoSortingByValue(value: String) =
    when(value) {
        INSCRICAO_SORTING_NOME_ASC -> InscricaoSorting.NOME_ASCENDING
        INSCRICAO_SORTING_NOME_DESC -> InscricaoSorting.NOME_DESCENDING
        INSCRICAO_SORTING_PRIORIDADE_ASC -> InscricaoSorting.PRIORIDADE_ASCENDING
        INSCRICAO_SORTING_PRIORIDADE_DESC -> InscricaoSorting.PRIORIDADE_DESCENDING
        else -> InscricaoSorting.NOME_ASCENDING
    }

const val DISCIPLINA_SORTING_NOME_ASC = "nome"
const val DISCIPLINA_SORTING_NOME_DESC = "-nome"
const val DISCIPLINA_SORTING_APTOS_ASC = "aptos"
const val DISCIPLINA_SORTING_APTOS_DESC = "-aptos"

fun getDisciplinaSortingByValue(value: String) =
    when(value) {
        DISCIPLINA_SORTING_NOME_ASC -> DisciplinaSorting.NAME_ASCENDING
        DISCIPLINA_SORTING_NOME_DESC -> DisciplinaSorting.NAME_DESCENDING
        DISCIPLINA_SORTING_APTOS_ASC -> DisciplinaSorting.ELIGIBLE_ASCENDING
        DISCIPLINA_SORTING_APTOS_DESC -> DisciplinaSorting.ELIGIBLE_DESCENDING
        else -> DisciplinaSorting.NAME_ASCENDING
    }

//fun getValueByAlunoSorting(sorting: AlunoSorting) =
//    when (sorting) {
//        AlunoSorting.MATRICULA_ASCENDING -> "matricula"
//        AlunoSorting.MATRICULA_DESCENDING -> "-matricula"
//        AlunoSorting.NOME_ASCENDING -> "nome"
//        AlunoSorting.NOME_DESCENDING -> "-nome"
//    }

//fun getValueByAlunoFilter(filter: AlunoFilter) = filter.name.lowercase()