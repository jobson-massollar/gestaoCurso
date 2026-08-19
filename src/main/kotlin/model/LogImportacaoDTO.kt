package model

import kotlinx.datetime.LocalDateTime
import kotlin.uuid.Uuid

class LogImportacaoDTO(id: Uuid?, val dtProcessamento: LocalDateTime): EntityDTO<LogImportacao>(id) {
    override fun toEntity() = LogImportacao.of(dtProcessamento)
}