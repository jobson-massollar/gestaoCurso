package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class ItemDiarioRepository: Repository<ItemDiario, IDAO.IItemDiarioDAO, ItemDiarioDTO>() {

    override val dao: IDAO.IItemDiarioDAO = DAOFactory.getDAO(DAOFactory.Type.DIARIO)

    //fun findByTurmaDisciplina(turma: String, disciplina: Disciplina): List<ItemDiario> = createEntityList(dao.findAll(turma, disciplina))

    fun findByTurmaDisciplina(turma: Turma, disciplina: Disciplina): List<ItemDiario> = createEntityList(dao.findAll(turma, disciplina))
}