package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class ItemHistoricoRepository: Repository<ItemHistorico, IDAO.IItemHistoricoDAO, ItemHistoricoDTO>() {

    override val dao: IDAO.IItemHistoricoDAO = DAOFactory.getDAO(DAOFactory.Type.HISTORICO)

    fun findByMatricula(matricula: String): List<ItemHistorico> =
        createEntityList(dao.findAll(matricula))

//    fun findAprovados(matricula: String): List<ItemHistorico> =
//        createEntityList(dao.findAprovados(matricula))
//
//    fun findMatriculados(matricula: String): List<ItemHistorico> =
//        createEntityList(dao.findMatriculados(matricula))
}