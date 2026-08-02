package model

import kotlin.uuid.Uuid

class ItemDiarioDTO(id: Uuid?, val matricula: String, val nome: String, val curso: Int, val depto: String, val versao: String, val codigo: String, val turma: String): EntityDTO<ItemDiario>(id) {

    override fun toEntity() = ItemDiario.of(matricula, nome, curso, depto, versao, codigo, turma)
}