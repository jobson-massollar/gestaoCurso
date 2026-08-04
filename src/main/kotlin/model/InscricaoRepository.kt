package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO

class InscricaoRepository : Repository<Inscricao, IDAO.IInscricaoDAO, InscricaoDTO>() {

    override val dao: IDAO.IInscricaoDAO = DAOFactory.getDAO(DAOFactory.Type.INSCRICAO)

}