package services.domain.persistence

import model.Aluno
import services.application.AlunoFilter

class AlunoRepository: Repository<Aluno, IDAO.IAlunoDAO, AlunoDTO>() {

    override val dao: IDAO.IAlunoDAO = DAOFactory.getDAO(DAOFactory.Type.ALUNO)

    fun findByFilter(filter: AlunoFilter, search: String) =
        when (filter) {
            AlunoFilter.ALL -> createEntityList(dao.findAll(search))
            AlunoFilter.ACTIVE -> createEntityList(dao.findAtivos(search))
        }

    fun findByMatricula(matricula: String): Aluno? = createEntity(dao.findByMatricula(matricula) ?: return null)
}