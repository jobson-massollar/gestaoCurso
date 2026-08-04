package model

import kotlin.uuid.Uuid

class TotalizacaoInscricaoDTO(uuid: Uuid?,
                              val codigo: String,
                              val nome: String,
                              val turma: String,
                              val aceitos: Int,
                              val faltaPreRequisito: Int,
                              val faltaVagas: Int,
                              val cancelados: Int): EntityDTO<TotalizacaoInscricao>(uuid) {

    override fun toEntity() = TotalizacaoInscricao(codigo, nome, turma, aceitos, faltaPreRequisito, faltaVagas, cancelados)
}