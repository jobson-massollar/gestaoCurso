package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class ItemHistoricoRepository: Repository<ItemHistorico, IDAO.IItemHistoricoDAO, ItemHistoricoDTO>() {

    override val dao: IDAO.IItemHistoricoDAO = DAOFactory.getDAO(DAOFactory.Type.HISTORICO)

    fun findByMatricula(aluno: Aluno): List<ItemHistorico> =
        createEntityList(dao.findByAluno(aluno))
}