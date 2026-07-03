package model

import kotlin.uuid.Uuid

class DisciplinaDTO(id: Uuid?, val versao: String, val codigo: String, val nome: String, val periodo: Int, val creditos: Int, val horas: Int, val tipo: String, val inscritos: Int): EntityDTO<Disciplina>(id) {
//    companion object {
//        fun fromEntity(d: Disciplina) = DisciplinaDTO(d.id, d.versao, d.codigo, d.nome, d.periodo, d.creditos, d.horas, d.tipo)
//    }

    override fun toEntity() = Disciplina.of(versao, codigo, nome, periodo, creditos, horas, tipo, inscritos)
}