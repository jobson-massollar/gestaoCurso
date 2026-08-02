package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class ItemDiarioRepository: Repository<ItemDiario, IDAO.IItemDiarioDAO, ItemDiarioDTO>() {

    override val dao: IDAO.IItemDiarioDAO = DAOFactory.getDAO(DAOFactory.Type.DIARIO)

    fun findAll(disciplina: Disciplina): List<ItemDiario> = createEntityList(dao.findAll(disciplina))
}