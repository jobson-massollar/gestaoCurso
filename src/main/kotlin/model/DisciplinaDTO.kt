package model

import kotlin.uuid.Uuid

class DisciplinaDTO(id: Uuid?,
                    val versao: String,
                    val codigo: String,
                    val nome: String,
                    val periodo: Int,
                    val creditos: Int,
                    val horas: Int,
                    val tipo: String,
                    val inscritos: Int): EntityDTO<Disciplina>(id) {

    override fun toEntity() = Disciplina.of(versao, codigo, nome, periodo, creditos, horas, tipo, inscritos)
}