package services.domain.persistence

import model.Disciplina

class DisciplinaRepository: Repository<Disciplina, IDAO.IDisciplinaDAO, DisciplinaDTO>() {

    override val dao: IDAO.IDisciplinaDAO = DAOFactory.getDAO(DAOFactory.Type.DISCIPLINA)

    fun findAll() = createEntityList(dao.findAll())

    fun findObrigatoriasFaltantes(matricula: String) = createEntityList(dao.findObrigatoriasFaltantes(matricula))
}