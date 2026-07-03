package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table

object Turmas: Table("vw_turmas_disciplinas") {
    val codigoTurma = varchar("turma", 20)
    val inscritosTurma = integer("inscritos_turma")
    val versao = varchar("versao", 6)
    val codigoDisciplina = varchar("codigo", 10)
    val nomeDisciplina = varchar("nome_disciplina", 100)
    val periodo = integer("periodo")
    val creditos = integer("creditos")
    val horas = integer("horas")
    val tipo = varchar("tipo", 60)
    val inscritosDisciplina = integer("inscritos")
}