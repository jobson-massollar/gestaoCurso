package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object AlunosAptosDisciplinas: Table("vw_alunos_aptos_disciplinas") {
    val matricula = varchar("matricula", 14)
    val codigo = varchar("codigo", 10)
}