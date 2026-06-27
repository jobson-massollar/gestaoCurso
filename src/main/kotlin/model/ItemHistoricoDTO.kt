package model

import kotlin.uuid.Uuid

class ItemHistoricoDTO(id: Uuid?, val matricula: String, val ano: Int, val periodo: Int, val descPeriodo: String, val versao: String, val codigo: String, val nome: String, val situacao: Int, val descricao: String, val nota: Float?, val creditos: Int, val horas: Int, val tipo: String): EntityDTO<ItemHistorico>(id) {

    companion object {
        fun fromEntity(h: ItemHistorico) = ItemHistoricoDTO(h.id, h.matricula, h.ano, h.periodo, h.descPeriodo, h.versao, h.codigo, h.nome, h.situacao, h.descricao, h.nota, h.creditos, h.horas, h.tipo)
    }

    override fun toEntity() = ItemHistorico.of(matricula, ano, periodo, descPeriodo, versao, codigo, nome, situacao, descricao, nota, creditos, horas, tipo)
}