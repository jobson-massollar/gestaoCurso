package model

import kotlin.uuid.Uuid

class PreRequisitoDTO(id: Uuid?, val versao: String, val codigo: String, val codigoPreReq: String): EntityDTO<PreRequisito>(id) {
    companion object {
        fun fromEntity(preReq: PreRequisito) = PreRequisitoDTO(preReq.id, preReq.versao, preReq.codigo, preReq.codigoPreReq)
    }

    override fun toEntity() =  PreRequisito.of(versao, codigo, codigoPreReq)
}