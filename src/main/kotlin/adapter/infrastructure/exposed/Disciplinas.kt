package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

abstract class DisciplinasBase(tableName: String): Table(tableName)  {
    val versao = varchar("versao", 6)
    val codigo = varchar("codigo", 10)
    val nome = varchar("nome", 100)
    val periodo = integer("periodo")
    val creditos = integer("creditos")
    val horas = integer("horas")
    val tipo = varchar("tipo", 60)
	
    init {
        //index("DISCIPLINA_VERSAO_CODIGO_AULA_UNIQUE", isUnique = true, versao, codigo, aula)
        index("DISCIPLINA_VERSAO_CODIGO_IDX", isUnique = false, versao, codigo)
    }
}

object Disciplinas: DisciplinasBase("vw_disciplinas")

object DisciplinasObrigatoriasFaltantes: DisciplinasBase("vw_obrigatorias_faltantes") {
    val matricula = varchar("matricula", 14)
	
	init {
        index("MATRICULA_IDX", isUnique = false, matricula)
    }
}