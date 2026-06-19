package adapter.infrastructure.exposed

import kotlinx.html.Entities
import model.APROVADO
import model.APROVADO_SEM_NOTA
import model.APROVEITAMENTO
import model.DISPENSA
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.selectAll
import services.domain.persistence.IDAO.IItemHistoricoDAO
import services.domain.persistence.ItemHistoricoDTO

class ItemHistoricoExposedDAO: IItemHistoricoDAO {
//    override fun insert(dto: ItemHistoricoDTO) {
//        transaction {
//            ItensHistorico.insert {
//                it[ItensHistorico.id] = dto.id!!
//                it[matricula] = dto.matricula
//                it[ano] = dto.ano
//                it[periodo] = dto.periodo
//                it[descPeriodo] = dto.descPeriodo
//                it[versao] = dto.versao
//                it[codigo] = dto.codigo
//                it[nome] = dto.nome
//                it[situacao] = dto.situacao
//                it[descricao] = dto.descricao
//                it[nota] = dto.nota
//                it[creditos] = dto.creditos
//                it[horas] = dto.horas
//                it[tipo] = dto.horas
//            }
//        }
//    }
//
//    override fun update(dto: ItemHistoricoDTO) {
//        transaction {
//            ItensHistorico.update({ ItensHistorico.id eq dto.id!! }) {
//                it[ItensHistorico.matricula] = dto.matricula
//                it[ano] = dto.ano
//                it[periodo] = dto.periodo
//                it[descPeriodo] = dto.descPeriodo
//                it[versao] = dto.versao
//                it[codigo] = dto.codigo
//                it[nome] = dto.nome
//                it[situacao] = dto.situacao
//                it[descricao] = dto.descricao
//                it[nota] = dto.nota
//                it[creditos] = dto.creditos
//                it[horas] = dto.horas
//            }
//        }
//    }
//
//    override fun delete(dto: ItemHistoricoDTO) {
//        transaction {
//            ItensHistorico.deleteWhere { ItensHistorico.id eq dto.id!! }
//        }
//    }

    override fun findAll(): List<ItemHistoricoDTO> =
        transaction {
            ItensHistorico
                .selectAll().map {
                    createDTO(it)
                }.toList()
        }

    override fun findAll(matricula: String): List<ItemHistoricoDTO> =
        transaction {
            ItensHistorico
                .selectAll()
                .where { ItensHistorico.matricula eq matricula }
                .map {
                    createDTO(it)
                }.toList()
        }

    override fun findAprovados(matricula: String): List<ItemHistoricoDTO> =
        transaction {
            ItensHistorico
                .selectAll()
                .where { ItensHistorico.matricula eq matricula and
                        ((ItensHistorico.situacao eq APROVADO) or
                         (ItensHistorico.situacao eq DISPENSA) or
                         (ItensHistorico.situacao eq APROVADO_SEM_NOTA) or
                         (ItensHistorico.situacao eq APROVEITAMENTO)) }
                .map {
                    createDTO(it)
                }.toList()
        }


    private fun createDTO(row: ResultRow): ItemHistoricoDTO =
        ItemHistoricoDTO(row[ItensHistorico.id],
            row[ItensHistorico.matricula],
            row[ItensHistorico.ano],
            row[ItensHistorico.periodo],
            row[ItensHistorico.descPeriodo],
            row[ItensHistorico.versao],
            row[ItensHistorico.codigo],
            row[ItensHistorico.nome],
            row[ItensHistorico.situacao],
            row[ItensHistorico.descricao],
            row[ItensHistorico.nota],
            row[ItensHistorico.creditos],
            row[ItensHistorico.horas],
            row[ItensHistorico.tipo])

//    override fun deleteAll() {
//        transaction {
//            ItensHistorico.deleteAll()
//        }
//    }
//
//    override fun batchInsert(dtos: List<ItemHistoricoDTO>) {
//        transaction {
//            ItensHistorico.batchInsert(
//                data = dtos,
//                shouldReturnGeneratedValues = false)
//                {dto ->
//                    this[ItensHistorico.id] = dto.id!!
//                    this[ItensHistorico.matricula] = dto.matricula
//                    this[ItensHistorico.ano] = dto.ano
//                    this[ItensHistorico.periodo] = dto.periodo
//                    this[ItensHistorico.descPeriodo] = dto.descPeriodo
//                    this[ItensHistorico.versao] = dto.versao
//                    this[ItensHistorico.codigo] = dto.codigo
//                    this[ItensHistorico.nome] = dto.nome
//                    this[ItensHistorico.situacao] = dto.situacao
//                    this[ItensHistorico.descricao] = dto.descricao
//                    this[ItensHistorico.nota] = dto.nota
//                    this[ItensHistorico.creditos] = dto.creditos
//                    this[ItensHistorico.horas] = dto.horas
//                }
//        }
//    }
}