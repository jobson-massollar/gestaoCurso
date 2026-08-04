package model

import kotlin.uuid.Uuid

class TurmaDisciplinaDTO(id: Uuid?,
                         val codigoTurma: String,
                         val inscritosTurma:Int,
                         val disciplinaDTO: DisciplinaDTO): EntityDTO<Turma>(id) {

    override fun toEntity() =  Turma.of(codigoTurma, inscritosTurma)
}