package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

abstract class DisciplinasBase(tableName: String): Table(tableName)  {
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10).index()
    val nome = varchar("nome", 100)
    val periodo = integer("periodo")
    val creditos = integer("creditos")
    val horas = integer("horas")
    val tipo = varchar("tipo", 60)
}

object Disciplinas: DisciplinasBase("vw_disciplinas") {
    val id = uuid("id")
}

object DisciplinasObrigatoriasFaltantes: DisciplinasBase("vw_obrigatorias_faltantes") {
    val matricula = varchar("matricula", 14)
}