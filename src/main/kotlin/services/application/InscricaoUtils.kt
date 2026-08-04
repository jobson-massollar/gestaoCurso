package services.application

import main.collator
import model.TotalizacaoInscricao

enum class InscricaoSorting(val comparator: Comparator<TotalizacaoInscricao>) {
    DISCIPLINA_ASCENDING(compareBy(collator) { it.nome }),
    DISCIPLINA_DESCENDING(compareByDescending(collator) { it.nome }),
    ACEITOS_ASCENDING(compareBy { it.aceitos }),
    ACEITOS_DESCENDING(compareByDescending { it.aceitos }),
    VAGAS_ASCENDING(compareBy { it.faltaVagas }),
    VAGAS_DESCENDING(compareByDescending { it.faltaVagas }),
}