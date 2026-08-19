package adapter.infrastructure.exposed

import model.Aluno
import model.Disciplina
import model.InscricaoDTO
import model.Turma
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import services.domain.persistence.IDAO

class InscricaoExposedDAO: IDAO.IInscricaoDAO {

    override fun findByCodeTurmaDisciplina(codigoTurma: String, codigoDisciplina: String): List<InscricaoDTO> =
        transaction {
            Inscricoes
                .selectAll()
                .where { (Inscricoes.codigo eq codigoDisciplina) and (Inscricoes.turma eq codigoTurma) }
                .map { createDTO(it) }
        }

    override fun findByAluno(aluno: Aluno): List<InscricaoDTO> =
        transaction {
            Inscricoes
                .selectAll()
                .where { Inscricoes.matricula eq aluno.matricula }
                .map { createDTO(it) }
        }

    override fun findByTurmaDisciplina(
        turma: Turma,
        disciplina: Disciplina
    ): List<InscricaoDTO> =
        transaction {
            Inscricoes
                .selectAll()
                .where { (Inscricoes.turma eq turma.codigo) and (Inscricoes.codigo eq disciplina.codigo) }
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