package adapter.infrastructure.exposed

import model.Aluno
import model.InscricaoDTO
import org.jetbrains.exposed.v1.core.ResultRow
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
                .map { createDTO(it) }
        }

    override fun findByAluno(aluno: Aluno): List<InscricaoDTO> =
        transaction {
            Inscricoes
                .selectAll()
                .where { Inscricoes.matricula eq aluno.matricula }
                .map { createDTO(it) }
        }

    private fun createDTO(row: ResultRow): InscricaoDTO =
        InscricaoDTO(
            id = row[Inscricoes.id],
            matricula = row[Inscricoes.matricula],
            nomeAluno = row[Inscricoes.nomeAluno],
            codigo = row[Inscricoes.codigo],
            nome = row[Inscricoes.nome],
            turma = row[Inscricoes.turma],
            situacao = row[Inscricoes.situacao],
            descricao = row[Inscricoes.descricao],
            ano = row[Inscricoes.ano],
            periodo = row[Inscricoes.periodo],
            dataSolicitacao = row[Inscricoes.dataSolicitacao],
            horaSolicitacao = row[Inscricoes.horaSolicitacao],
            dataProcessaento = row[Inscricoes.dataProcessamento]
        )
}