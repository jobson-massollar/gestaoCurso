package adapter.infrastructure.exposed

import model.InscricaoDTO
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO

class InscricaoExposedDAO: IDAO.IInscricaoDAO {

    override fun findByDisciplina(codigo: String, turma: String): List<InscricaoDTO> =
        transaction {
            Inscricoes
                .selectAll()
                .where { (Inscricoes.codigo eq codigo) and (Inscricoes.turma eq turma) }
                . map {
                    InscricaoDTO(
                        id = it[Inscricoes.id],
                        matricula = it[Inscricoes.matricula],
                        nomeAluno = it[Inscricoes.nomeAluno],
                        codigo = it[Inscricoes.codigo],
                        nome = it[Inscricoes.nome],
                        turma = it[Inscricoes.turma],
                        situacao = it[Inscricoes.situacao],
                        descricao = it[Inscricoes.descricao],
                        ano = it[Inscricoes.ano],
                        periodo = it[Inscricoes.periodo],
                        dataSolicitacao = it[Inscricoes.dataSolicitacao],
                        horaSolicitacao = it[Inscricoes.horaSolicitacao],
                        dataProcessaento = it[Inscricoes.dataProcessamento]
                    )
                }
        }
}