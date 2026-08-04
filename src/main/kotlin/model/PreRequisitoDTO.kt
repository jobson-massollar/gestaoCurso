package model

import kotlin.uuid.Uuid

class PreRequisitoDTO(id: Uuid?,
                      val versao: String,
                      val codigo: String,
                      val codigoPreReq: String): EntityDTO<PreRequisito>(id) {

    override fun toEntity() =  PreRequisito.of(versao, codigo, codigoPreReq)
}