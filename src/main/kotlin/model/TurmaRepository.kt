package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.ITurmaDAO

class TurmaRepository: Repository<Turma, ITurmaDAO, TurmaDisciplinaDTO>() {

    override val dao: ITurmaDAO = DAOFactory.getDAO(DAOFactory.Type.TURMA)

    fun findAll(): List<Turma> {
        val turmaMap = mutableMapOf<String, Turma>()

        // Eager loading de Turma + Disciplina
        // Pega cada DTO que contem Turma + Disciplina e gera a Turma.
        // Adiciona ou recupera a Turma de um mapa
        // Cria a disciplina e adiciona na lista de disciplinas da turma
        dao.findAll().forEach { dto ->
            val turma = turmaMap.getOrPut(dto.codigoTurma) {
                            createEntity(dto)
                        }
            turma.addDisciplina(dto.disciplinaDTO.toEntity())
        }

        return turmaMap.values.toList()
    }
}