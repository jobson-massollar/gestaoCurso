package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.Uuid

class InscricaoDTO(id: Uuid?,
                   val matricula: String,
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
                   val dataProcessaento: LocalDate): EntityDTO<Inscricao>(id) {

    override fun toEntity() = Inscricao.of(matricula,
        nomeAluno,
        codigo,
        nome,
        turma,
        situacao,
        descricao,
        ano,
        periodo,
        dataSolicitacao,
        horaSolicitacao,
        dataProcessaento)
}