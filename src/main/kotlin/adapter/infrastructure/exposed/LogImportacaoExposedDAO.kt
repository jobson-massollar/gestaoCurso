package adapter.infrastructure.exposed

import model.LogImportacaoDTO
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO.ILogImportacaoDAO

class LogImportacaoExposedDAO: ILogImportacaoDAO {

    override fun findLast(): LogImportacaoDTO? =
        transaction {
            LogImportacoes
                .selectAll()
                .orderBy(LogImportacoes.dtProcessamento, SortOrder.DESC)
                .limit(1)
                .map {
                    LogImportacaoDTO(it[LogImportacoes.id], it[LogImportacoes.dtProcessamento])
                }
                .firstOrNull()
        }
}