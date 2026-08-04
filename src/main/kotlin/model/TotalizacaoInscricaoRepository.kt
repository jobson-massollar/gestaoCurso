package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.ITotalizacaoInscricaoDAO

class TotalizacaoInscricaoRepository: Repository<TotalizacaoInscricao, ITotalizacaoInscricaoDAO, TotalizacaoInscricaoDTO>() {

    override val dao: ITotalizacaoInscricaoDAO = DAOFactory.getDAO(DAOFactory.Type.TOTAL_INSCRICAO)

    fun findTotalizacoes() = createEntityList(dao.findTotalizacoes())
}