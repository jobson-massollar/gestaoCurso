package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.DisciplinaDTO
import services.domain.persistence.IDAO.IDisciplinaDAO
import kotlin.uuid.Uuid

class DisciplinaExposedDAO: IDisciplinaDAO {
//    override fun insert(dto: DisciplinaDTO) {
//        transaction {
//            Disciplinas.insert {
//                it[Disciplinas.id] = dto.id!!
//                it[versao] = dto.versao
//                it[codigo] = dto.codigo
//                it[nome] = dto.nome
//                it[periodo] = dto.periodo
//                it[creditos] = dto.creditos
//                it[horas] = dto.horas
//                it[tipo] = dto.tipo
//                it[situacao] = dto.situacao
//                it[aula] = dto.aula
//            }
//        }
//    }

//    override fun update(dto: DisciplinaDTO) {
//        transaction {
//            Disciplinas.update({ Disciplinas.id eq dto.id!! }) {
//                it[versao] = dto.versao
//                it[codigo] = dto.codigo
//                it[nome] = dto.nome
//                it[periodo] = dto.periodo
//                it[creditos] = dto.creditos
//                it[horas] = dto.horas
//                it[tipo] = dto.tipo
//                it[situacao] = dto.situacao
//                it[aula] = dto.aula
//            }
//        }
//    }

//    override fun delete(dto: DisciplinaDTO) {
//        transaction {
//            Disciplinas.deleteWhere { Disciplinas.id eq dto.id!! }
//        }
//    }

    override fun findAll(): List<DisciplinaDTO> =
        transaction {
            Disciplinas
                .selectAll()
                .map {
                    createDTO(it, Disciplinas, it[Disciplinas.id])
            }.toList()
        }

    override fun findObrigatoriasFaltantes(matricula: String) =
        transaction {
            DisciplinasObrigatoriasFaltantes
                .selectAll()
                .where { DisciplinasObrigatoriasFaltantes.matricula eq matricula }
                .map {
                    createDTO(it, DisciplinasObrigatoriasFaltantes, null)
            }.toList()
        }

    private fun createDTO(row: ResultRow, disciplinas: DisciplinasBase, id: Uuid? = null) =
        DisciplinaDTO(id,
            row[disciplinas.versao],
            row[disciplinas.codigo],
            row[disciplinas.nome],
            row[disciplinas.periodo],
            row[disciplinas.creditos],
            row[disciplinas.horas],
            row[disciplinas.tipo])

//    override fun deleteAll() {
//        transaction {
//            Disciplinas.deleteAll()
//        }
//    }

//    override fun batchInsert(dtos: List<DisciplinaDTO>) {
//        transaction {
//            Disciplinas.batchInsert(
//                data = dtos,
//                shouldReturnGeneratedValues = false)
//                {dto ->
//                    this[Disciplinas.id] = dto.id!!
//                    this[versao] = dto.versao
//                    this[codigo] = dto.codigo
//                    this[nome] = dto.nome
//                    this[periodo] = dto.periodo
//                    this[creditos] = dto.creditos
//                    this[horas] = dto.horas
//                    this[tipo] = dto.tipo
//                    this[situacao] = dto.situacao
//                    this[aula] = dto.aula
//                }
//        }
//    }
}