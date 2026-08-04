package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class Inscricao private constructor(val matricula: String,
                                    val nomeAluno: String,
                                    val codigo: String,
                                    val nome: String,
                                    val turma: String,
                                    val situacao: String,
                                    val descricao: String,
                                    val ano: Int,
                                    val periodo: Int,
                                    val dataSolicitacao: LocalDate,
                                    val horaSolicitacao: LocalTime,
                                    val dataProcessaento: LocalDate): Entity() {

    override fun equals(other: Any?): Boolean =
        if (other is Inscricao)
            matricula == other.matricula && codigo == other.codigo
        else
            false

    override fun hashCode() = (matricula + codigo).hashCode()

    companion object {
        fun of(matricula: String, nomeAluno: String, codigo: String, nome: String, turma: String, situacao: String, descricao: String, ano: Int, periodo: Int, dataSolicitacao: LocalDate, horaSolicitacao: LocalTime, dataProcessaento: LocalDate) =
            Inscricao(matricula, nomeAluno, codigo, nome, turma, situacao, descricao, ano, periodo, dataSolicitacao, horaSolicitacao, dataProcessaento)
    }
}