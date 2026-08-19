package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime

object LogImportacoes : Table("log_importacao") {
    val id = uuid("id")
    val dtProcessamento = datetime("dt_processamento") // 👈 Mapeia para kotlinx.datetime.LocalDateTime

    override val primaryKey = PrimaryKey(id)
}