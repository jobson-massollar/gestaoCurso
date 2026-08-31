package services.application

import main.collator
import model.Disciplina
import kotlin.Comparator

enum class DisciplinaSorting(val comparator: Comparator<Disciplina>) {
    NAME_ASCENDING(compareBy(collator) { it.nome }),
    NAME_DESCENDING(compareByDescending(collator) { it.nome }),
    ELIGIBLE_ASCENDING( compareBy { it.podemCursar
        .size}),
    ELIGIBLE_DESCENDING(compareByDescending { it.podemCursar
        .size})
}