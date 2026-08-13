package adapter.infrastructure.exposed

import model.Disciplina
import model.ItemDiarioDTO
import model.Turma
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO.IItemDiarioDAO

class ItemDiarioExposedDAO: IItemDiarioDAO {

    override fun findAll(turma: Turma, disciplina: Disciplina): List<ItemDiarioDTO> =
        transaction {
            ItensDiario
                .selectAll()
                .where {
                    (ItensDiario.turma eq turma.codigo) and
                    (ItensDiario.versao eq disciplina.versao) and
                    (ItensDiario.codigo eq disciplina.codigo)
                }
                .map {
                    ItemDiarioDTO(
                        it[ItensDiario.id],
                        it[ItensDiario.matricula],
                        it[ItensDiario.nome],
                        it[ItensDiario.curso],
                        it[ItensDiario.depto],
                        it[ItensDiario.versao],
                        it[ItensDiario.codigo],
                        it[ItensDiario.turma]
                    )
                }
        }
}