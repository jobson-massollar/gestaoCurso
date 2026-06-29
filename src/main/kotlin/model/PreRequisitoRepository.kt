package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.IPreRequisitoDAO

class PreRequisitoRepository: Repository<PreRequisito, IPreRequisitoDAO, PreRequisitoDTO>() {

    override val dao: IPreRequisitoDAO = DAOFactory.getDAO(DAOFactory.Type.PRE_REQUISITO)

}