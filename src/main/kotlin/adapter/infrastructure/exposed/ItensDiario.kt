package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object ItensDiario: Table("itens_diario") {
    val id = uuid("id")
    val matricula = varchar("matricula", 14)
    val nome = varchar("nome", 100)
    val curso = integer("curso")
    val depto = varchar("depto", 5)
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10)
    val turma = varchar("turma", 20)
}