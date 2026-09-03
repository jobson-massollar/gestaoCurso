package model

import kotlin.uuid.Uuid

class ItemHistoricoDTO(id: Uuid?,
                       val matricula: String,
                       val ano: Int,
                       val periodo: Int,
                       val descPeriodo: String,
                       val versao: String,
                       val codigo: String,
                       val nome: String,
                       val situacao: Int,
                       val descricao: String,
                       val nota: Float?,
                       val creditos: Int,
                       val horas: Int,
                       val tipo: String): EntityDTO<ItemHistorico>(id) {

    override fun toEntity() = ItemHistorico.of(matricula,
        ano,
        periodo,
        descPeriodo,
        versao,
        codigo,
        nome,
        StatusItemHistorico.fromCodigo(situacao),
        descricao,
        nota,
        creditos,
        horas,
        TipoDisciplina.fromDescricao(tipo))
}