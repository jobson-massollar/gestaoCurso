package services.domain.persistence

import model.ItemHistorico

class ItemHistoricoRepository: Repository<ItemHistorico, IDAO.IItemHistoricoDAO, ItemHistoricoDTO>() {

    override val dao: IDAO.IItemHistoricoDAO = DAOFactory.getDAO(DAOFactory.Type.HISTORICO)

    fun findByMatricula(matricula: String): List<ItemHistorico> =
        createEntityList(dao.findAll(matricula))

    fun findAprovados(matricula: String): List<ItemHistorico> =
        createEntityList(dao.findAprovados(matricula))
}