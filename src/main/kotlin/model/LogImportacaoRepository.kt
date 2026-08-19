package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.ILogImportacaoDAO

class LogImportacaoRepository: Repository<LogImportacao, ILogImportacaoDAO, LogImportacaoDTO>() {

    override val dao: ILogImportacaoDAO = DAOFactory.getDAO(DAOFactory.Type.LOG_IMPORTACAO)

    fun findLast(): LogImportacao? = createEntity(dao.findLast() ?: return null)
}