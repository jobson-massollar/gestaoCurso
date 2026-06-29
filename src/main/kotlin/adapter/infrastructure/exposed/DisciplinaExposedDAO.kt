package adapter.infrastructure.exposed

import model.Aluno
import model.Disciplina
import model.DisciplinaDTO
import model.ItemHistorico
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
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
                    createDTO(it, Disciplinas)
            }.toList()
        }

    override fun findObrigatoriasFaltantes(aluno: Aluno) =
        transaction {
            DisciplinasObrigatoriasFaltantes
                .selectAll()
                .where { DisciplinasObrigatoriasFaltantes.matricula eq aluno.matricula }
                .map {
                    createDTO(it, DisciplinasObrigatoriasFaltantes)
                }.toList()
        }

    override fun findByItemHistorico(itemHistorico: ItemHistorico) =
        transaction {
            Disciplinas
                .selectAll()
                .where { (Disciplinas.versao eq itemHistorico.versao) and (Disciplinas.codigo eq itemHistorico.codigo) }
                .limit(1)
                .map {
                    createDTO(it, Disciplinas)
                }.first()
        }

    override fun findPreRequisitos(disciplina: Disciplina) =
        transaction {
            Disciplinas
                .join(
                    PreRequisitos,
                    JoinType.INNER) {
                        (Disciplinas.versao eq PreRequisitos.versao) and
                        (Disciplinas.codigo eq PreRequisitos.codigoPreReq)
                    }
                .selectAll()
                .where { PreRequisitos.codigo eq disciplina.codigo }
                .map {
                    createDTO(it, Disciplinas)
                }
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