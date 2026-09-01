package services.application

import main.collator
import model.Inscricao
import model.TotalizacaoInscricao

enum class TotalizacaoInscricaoSorting(val comparator: Comparator<TotalizacaoInscricao>) {
    DISCIPLINA_ASCENDING(compareBy(collator) { it.nome }),
    DISCIPLINA_DESCENDING(compareByDescending(collator) { it.nome }),
    SOLICITADOS_ASCENDING(compareBy { it.solicitados }),
    SOLICITADOS_DESCENDING(compareByDescending { it.solicitados }),
    ACEITOS_ASCENDING(compareBy { it.aceitos }),
    ACEITOS_DESCENDING(compareByDescending { it.aceitos }),
    VAGAS_ASCENDING(compareBy { it.faltaVagas }),
    VAGAS_DESCENDING(compareByDescending { it.faltaVagas }),
}

enum class InscricaoSorting(val comparator: Comparator<Inscricao>) {
    NOME_ASCENDING(compareBy(collator) { it.nomeAluno }),
    NOME_DESCENDING(compareByDescending(collator) { it.nomeAluno }),
    PRIORIDADE_ASCENDING({ i1, i2 -> compareInscricao(i1, i2, 1) }),
    PRIORIDADE_DESCENDING({ i1, i2 -> compareInscricao(i1, i2, -1) }),
}

private fun compareInscricao(i1: Inscricao, i2: Inscricao, fator: Int) =
    if (i1.prioridade != i2.prioridade)
        (i1.prioridade-i2.prioridade) * fator
    else if (i1.dataSolicitacao != i2.dataSolicitacao)
        i1.dataSolicitacao.compareTo(i2.dataSolicitacao)
    else
        i1.horaSolicitacao.compareTo(i2.horaSolicitacao)
