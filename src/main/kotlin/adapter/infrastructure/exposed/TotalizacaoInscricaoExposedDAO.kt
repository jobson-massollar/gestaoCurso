package adapter.infrastructure.exposed

import model.TotalizacaoInscricaoDTO
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO.ITotalizacaoInscricaoDAO

class TotalizacaoInscricaoExposedDAO: ITotalizacaoInscricaoDAO {

    override fun findTotalizacoes(): List<TotalizacaoInscricaoDTO> =
        transaction {
            TotalizacaoInscricoes
                .selectAll()
                .map {
                    TotalizacaoInscricaoDTO(
                        null,
                        it[TotalizacaoInscricoes.codigo],
                        it[TotalizacaoInscricoes.nome],
                        it[TotalizacaoInscricoes.turma],
                        it[TotalizacaoInscricoes.aceitos],
                        it[TotalizacaoInscricoes.faltaPreRequisitos],
                        it[TotalizacaoInscricoes.faltaVagas],
                        it[TotalizacaoInscricoes.cancelados]
                    )
                }
            }
}