package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

const val ACEITA = 2
const val MATRICULA_CANCELADA = 3
const val FALTA_PRE_REQ = 4
const val JA_MATRICULADO = 7
const val FALTA_VAGA = 9
const val TRANCAMENTO_TOTAL = 15
const val SOLICITACAO_CANCELADA = 99

class Inscricao private constructor(val matricula: String,
                                    val nomeAluno: String,
                                    val codigo: String,
                                    val nome: String,
                                    val turma: String,
                                    val prioridade: Int,
                                    val situacao: Int,
                                    val descricao: String,
                                    val ano: Int,
                                    val periodo: Int,
                                    val dataSolicitacao: LocalDate,
                                    val horaSolicitacao: LocalTime,
                                    val dataProcessamento: LocalDate?): Entity() {

    val aluno: Aluno? by lazy {
        RepositoryFactory.get(AlunoRepository::class).findByInscricao(this)
    }

    override fun equals(other: Any?): Boolean =
        if (other is Inscricao)
            matricula == other.matricula && codigo == other.codigo
        else
            false

    override fun hashCode() = (matricula + codigo).hashCode()

    companion object {
        fun of(matricula: String, nomeAluno: String, codigo: String, nome: String, turma: String, prioridade: Int, situacao: Int, descricao: String, ano: Int, periodo: Int, dataSolicitacao: LocalDate, horaSolicitacao: LocalTime, dataProcessaento: LocalDate?) =
            Inscricao(matricula, nomeAluno, codigo, nome, turma, prioridade, situacao, descricao, ano, periodo, dataSolicitacao, horaSolicitacao, dataProcessaento)
    }
}