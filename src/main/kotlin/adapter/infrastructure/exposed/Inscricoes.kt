package adapter.infrastructure.exposed

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

abstract class InscricoesBase(name: String): Table(name) {
    val id = uuid("id")
    val matricula = varchar("matricula", 14).uniqueIndex()
    val nomeAluno = varchar("nome_aluno", 100)
    val codigo = varchar("codigo", 10)
    val nome = varchar("nome", 100)
    val turma = varchar("turma", 10)
    val situacao = integer("situacao")
    val descricao = varchar("descricao", 50)
    val ano = integer("ano")
    val periodo = integer("periodo")
    val dataSolicitacao = date("dt_solicitacao")
    val horaSolicitacao = time("hora_solicitacao")
    val dataProcessamento = date("dt_processamento").nullable()
}

object Inscricoes: InscricoesBase("Inscricoes") {
    override val primaryKey = PrimaryKey(Inscricoes.id)
}

object SituacaoFinalInscricoes: InscricoesBase("vw_situacao_final_inscricoes")

object TotalizacaoInscricoes: Table("vw_total_inscricoes") {
    val codigo = varchar("codigo", 10)
    val nome = varchar("nome", 100)
    val turma = varchar("turma", 10)
    val solicitados = integer("solicitados")
    val aceitos = integer("aceitos")
    val faltaPreRequisitos = integer("falta_pr")
    val faltaVagas = integer("falta_vagas")
    val cancelados = integer("cancelados")
}