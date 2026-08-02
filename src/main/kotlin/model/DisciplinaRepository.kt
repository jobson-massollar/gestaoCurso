package model

import services.domain.persistence.DAOFactory
import services.domain.persistence.IDAO.IDisciplinaDAO

class DisciplinaRepository: Repository<Disciplina, IDisciplinaDAO, DisciplinaDTO>() {

    override val dao: IDisciplinaDAO = DAOFactory.getDAO(DAOFactory.Type.DISCIPLINA)

    fun findAll() = createEntityList(dao.findAll())

    fun findObrigatoriasFaltantes(aluno: Aluno) = createEntityList(dao.findObrigatoriasFaltantes(aluno))

    fun findByItemHistorico(itemHistorico: ItemHistorico) = createEntity(dao.findByItemHistorico(itemHistorico))

    fun findPreRequisitos(disciplina: Disciplina) = createEntityList(dao.findPreRequisitos(disciplina))

    fun findByCode(versao: String, codigo: String) = createEntity(dao.findByCode(versao, codigo))
}