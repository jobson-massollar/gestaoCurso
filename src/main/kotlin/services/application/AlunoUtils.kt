package services.application

import main.collator
import model.Aluno

enum class AlunoSorting(val comparator: Comparator<Aluno>) {
    MATRICULA_ASCENDING(compareBy(collator) { it.matricula }),
    MATRICULA_DESCENDING(compareByDescending(collator) { it.matricula }),
    NOME_ASCENDING(compareBy(collator) { it.nome }),
    NOME_DESCENDING(compareByDescending(collator) { it.nome });
}

enum class AlunoFilter {
    ALL,
    ACTIVE,
    GRADUATED,
    GRADUATING
}



